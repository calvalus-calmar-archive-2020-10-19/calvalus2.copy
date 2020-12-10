package com.bc.calvalus.production.local;

import com.bc.calvalus.commons.*;
import com.bc.calvalus.production.*;
import com.bc.calvalus.staging.Staging;
import com.bc.calvalus.staging.StagingService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/**
 * A production type that does nothing.
 */
class DummyProductionType implements ProductionType {
    private final LocalProcessingService processingService;
    private final StagingService stagingService;

    public DummyProductionType(LocalProcessingService processingService, StagingService stagingService) {
        this.processingService = processingService;
        this.stagingService = stagingService;
    }

    @Override
    public String getName() {
        return "dummy";
    }

    @Override
    public Production createProduction(ProductionRequest productionRequest) throws ProductionException {
        String name = productionRequest.getString("name", null);
        if (name == null) {
            name = "Doing something";
        }
        final String userName = productionRequest.getUserName();

        Workflow.Sequential sequential = new Workflow.Sequential();
        sequential.add(new AbstractWorkflowItem() {
            String jobId;

            @Override
            public void submit() {
                jobId = processingService.submitJob();
                setSubmitTime(new Date());
            }

            @Override
            public void kill() throws WorkflowException {
                try {
                    processingService.killJob(userName, jobId);
                } catch (IOException e) {
                    throw new WorkflowException("Failed to kill job: " + e.getMessage(), e);
                }
            }

            @Override
            public void updateStatus() {
                if (jobId != null) {
                    setStatus(processingService.getJobStatus(jobId));
                }
            }

            @Override
            public Object[] getJobIds() {
                return jobId != null ? new Object[]{jobId} : new Object[0];
            }
        });

        String productionId = Production.createId(productionRequest.getProductionType());
        String stagingDir = productionRequest.getStagingDirectory(productionId);
        boolean autoStaging = productionRequest.isAutoStaging();
        return new Production(productionId,
                              name,
                              null,
                              stagingDir,
                              autoStaging,
                              productionRequest,
                              sequential);
    }

    @Override
    public Staging createStaging(final Production production) throws ProductionException {
        if (production.getStagingPath() == null) {
            return null;
        }
        DummyStaging dummyStaging = new DummyStaging(production);
        try {
            stagingService.submitStaging(dummyStaging);
        } catch (IOException e) {
            throw new ProductionException(e);
        }
        return dummyStaging;
    }

    @Override
    public boolean accepts(ProductionRequest productionRequest) {
        return true;
    }

    private class DummyStaging extends ProductionStaging {

        public DummyStaging(Production production) {
            super(production);
        }

        @Override
        public void performStaging() throws Throwable {
            Production production = getProduction();
            final File outputFile = new File(stagingService.getStagingDir(), production.getStagingPath());
            production.setStagingStatus(new ProcessStatus(ProcessState.RUNNING, 0.0f));
            if (!outputFile.exists()) {
                File parentFile = outputFile.getParentFile();
                if (parentFile != null) {
                    parentFile.mkdirs();
                }
                FileOutputStream stream = new FileOutputStream(outputFile);
                byte[] buffer = new byte[1024 * 1024];
                try {
                    for (int i = 0; i < 32; i++) {
                        if (isCancelled()) {
                            return;
                        }
                        Thread.sleep(500);
                        production.setStagingStatus(new ProcessStatus(ProcessState.RUNNING, i / 32f));
                        Arrays.fill(buffer, (byte) i);
                        stream.write(buffer);
                    }
                } finally {
                    stream.close();
                }
            }
            production.setStagingStatus(new ProcessStatus(ProcessState.COMPLETED, 1f));
        }

        @Override
        public void cancel() {
            super.cancel();
            getProduction().setStagingStatus(new ProcessStatus(ProcessState.CANCELLED));
        }
    }
}
