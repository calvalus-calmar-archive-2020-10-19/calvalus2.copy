/*
 * Copyright (C) 2010 Brockmann Consult GmbH (info@brockmann-consult.de)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, see http://www.gnu.org/licenses/
 */

package com.bc.calvalus.commons;

import java.util.Date;

/**
 * A workflow item. Clients should not implement this interface directly. Instead, they should
 * use {@link AbstractWorkflowItem} as base class.
 *
 * @author MarcoZ
 * @author Norman
 */
public interface WorkflowItem {
    /**
     * Submits this work item's job to the underlying job engine so that a new process is spawn.
     * Usually, implementations of this method should return immediately after the job has been submitted.
     * In rare cases the method blocks, e.g. until the associated process has terminated.
     *
     * @throws WorkflowException If the submission fails.
     */
    void submit() throws WorkflowException;

    /**
     * Kills this work item's process in the underlying job engine.
     * Usually, implementations of this method should return immediately after the kill request has been submitted.
     * In rare cases the method blocks, e.g. until the job has been terminated.
     *
     * @throws WorkflowException If the kill request fails.
     */
    void kill() throws WorkflowException;

    /**
     * Asks this workflow item to update its process status from the underlying job engine.
     */
    void updateStatus();

    /**
     * @return The current workflow item's process status.
     */
    ProcessStatus getStatus();

    /**
     * @param status The new workflow item's process status.
     */
    void setStatus(ProcessStatus status);

    /**
     * Adds a new workflow status change listener to the workflow.
     * The listener is called each time the status of this workflow item's
     * process changes.
     *
     * @param listener The new workflow status change listener.
     */
    void addWorkflowStatusListener(WorkflowStatusListener listener);

    // create ProxWorkflows when we need a factory that creates the correct Workflow for a production, need workflow persistence (nf)

    /**
     * Gets the identifiers of all the jobs that this workflow is managing.
     * The type of the job identifiers depends on the underlying job engine(s) used.
     * <p/>
     * <i>
     * TODO - nf/nf 2011-09-04 The only use of this method is currently to persist workflows.
     * But it it is actually better to perform serialisation/deserialisation by the {@code WorkflowItem} itself.
     * Also, not all types of workflow items have job IDs. </i>
     *
     * @return The array of job identifiers.
     */
    Object[] getJobIds();

    /**
     * @return The array of child items. The array will be empty, if there are no children.
     */
    WorkflowItem[] getItems();

    /**
     * @return The time when this workflow item has been submitted. It will be {@code null},
     *         if this item has not been submitted.
     */
    Date getSubmitTime();

    /**
     * @return The time when processing for this workflow item has been started. It will be {@code null},
     *         if this item has not been started.
     */
    Date getStartTime();

    /**
     * @return The time when processing for this workflow item has stopped.
     *         This means either the item has completed, has been canceled or an error occurred.
     *         It will be {@code null}, if this item has not been stopped.
     */
    Date getStopTime();
}
