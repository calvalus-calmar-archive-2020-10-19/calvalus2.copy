<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html"
                version="1.0"
                encoding="ISO-8859-1"
                omit-xml-declaration="yes"
                standalone="yes"
                doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
                doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
                cdata-section-elements="value"
                indent="yes"
                media-type="text/html"/>

    <xsl:template match="/">

        <!--
            Note: In order to further develop test this XSD,
                  run com.bc.calvalus.processing.ma.ReportGeneratorTest.

            TODO - add more information here (nf)
            * Provide processing statistics, e.g. total time, #tasks, #bytes, #input/output records, hadoop counters
            * Make the HTML nicer
        -->

        <html>
            <head>
                <title>Match-up Analysis</title>
                <link rel="stylesheet" type="text/css" href="styleset.css"/>
            </head>
            <body>

                <h2>Match-up Analysis</h2>

                <h3>Processing Information</h3>

                <table>
                    <tr>
                        <td class="name">Performed at:</td>
                        <td class="value">
                            <xsl:value-of select="analysisSummary/performedAt"/>
                        </td>
                    </tr>
                    <xsl:for-each select="analysisSummary/recordCounts/recordCount">
                        <tr>
                             <td class="name">Number of match-ups: <xsl:value-of select="name"/></td>
                             <td class="value"><xsl:value-of select="value"/>
                             </td>
                        </tr>
                    </xsl:for-each>
                </table>

                <h3>Analysis Parameters</h3>

                <table>
                    <xsl:for-each select="analysisSummary/configuration/property">
                        <xsl:choose>
                            <xsl:when test="name='calvalus.calvalus.bundle'">
                                <tr>
                                    <td class="name">Calvalus software bundle:</td>
                                    <td class="value">
                                        <xsl:value-of select="value"/>
                                    </td>
                                </tr>
                            </xsl:when>
                            <xsl:when test="name='calvalus.snap.bundle'">
                                <tr>
                                    <td class="name">SNAP software bundle:</td>
                                    <td class="value">
                                        <xsl:value-of select="value"/>
                                    </td>
                                </tr>
                            </xsl:when>
                            <xsl:when test="name='calvalus.l2.bundle'">
                                <tr>
                                    <td class="name">Level-2 software bundle:</td>
                                    <td class="value">
                                        <xsl:value-of select="value"/>
                                    </td>
                                </tr>
                            </xsl:when>
                            <xsl:when test="name='calvalus.l2.operator'">
                                <tr>
                                    <td class="name">Level-2 processor name:</td>
                                    <td class="value">
                                        <xsl:value-of select="value"/>
                                    </td>
                                </tr>
                            </xsl:when>
                            <xsl:when test="name='calvalus.l2.parameters'">
                                <tr>
                                    <td class="name">Level-2 processor parameters:</td>
                                    <td class="value">
                                        <xsl:copy-of select="value"/>
                                    </td>
                                </tr>
                            </xsl:when>
                            <xsl:when test="name='calvalus.ma.parameters'">
                                <tr>
                                    <td class="name">Input reference dataset:</td>
                                    <td class="value">
                                        <xsl:value-of select="value/parameters/recordSourceUrl"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="name">Output group name:</td>
                                    <td class="value">
                                        <xsl:value-of select="value/parameters/outputGroupName"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="name">Output date/time format:</td>
                                    <td class="value">
                                        <xsl:value-of select="value/parameters/outputTimeFormat"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="name">Macro pixel size:</td>
                                    <td class="value">
                                        <xsl:value-of select="value/parameters/macroPixelSize"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="name">Max. time difference (h):</td>
                                    <td class="value">
                                        <xsl:value-of select="value/parameters/maxTimeDifference"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="name">Good-pixel expression:</td>
                                    <td class="value">
                                        <xsl:value-of select="value/parameters/goodPixelExpression"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="name">Good-record expression:</td>
                                    <td class="value">
                                        <xsl:value-of select="value/parameters/goodRecordExpression"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="name">Copy input:</td>
                                    <td class="value">
                                        <xsl:value-of select="value/parameters/copyInput"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="name">Filter mean coefficient:</td>
                                    <td class="value">
                                        <xsl:value-of select="value/parameters/filteredMeanCoeff"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="name">Filter overlapping Match-ups:</td>
                                    <td class="value">
                                        <xsl:value-of select="value/parameters/filterOverlapping"/>
                                    </td>
                                </tr>
                            </xsl:when>
                        </xsl:choose>
                    </xsl:for-each>
                </table>

                <h3>Scatter plots</h3>

                <table>
                    <xsl:for-each select="analysisSummary/dataset">
                        <tr>
                            <td class="dataset-info">
                                <table>
                                    <tr>
                                        <td class="name">Satellite variable:</td>
                                        <td class="value">
                                            <xsl:value-of select="satelliteVariable"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="name">Reference variable:</td>
                                        <td class="value">
                                            <xsl:value-of select="referenceVariable"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="name">Number of data points:</td>
                                        <td class="value">
                                            <xsl:value-of select="statistics/numDataPoints"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="name">Lin. regression interception:</td>
                                        <td class="value">
                                            <xsl:value-of select="statistics/regressionInter"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="name">Lin. regression slope:</td>
                                        <td class="value">
                                            <xsl:value-of select="statistics/regressionSlope"/>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                            <td class="dataset-graph">
                                <img>
                                    <xsl:attribute name="src">
                                        <xsl:value-of select="statistics/scatterPlotImage"/>
                                    </xsl:attribute>
                                </img>
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>

            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
