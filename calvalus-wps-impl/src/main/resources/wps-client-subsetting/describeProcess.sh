#!/bin/bash

# This is a script to send DescribeProcess request to Calvalus WPS.
# This script takes 1 parameter : the processor ID. For including all parameters, use 'all' as the processor ID.
# The response is displayed on the screen.
# Usage : describeProcess.sh [process ID]
# Example : describeProcess.sh urbantep-subsetting~1.0~Subset
#           describeProcess.sh all

function usage {
    echo "---------------------------------------"
    echo "USAGE: describeProcess.sh <process_id>"
    echo "---------------------------------------"
}

if [ "$1" = "-h" ] ; then
    usage
    exit 0
elif [ -z "$1" ] ; then
    echo "process ID is missing"
    usage
    exit 0
fi

PROCESS_ID=$1

read -p "Enter User Name: " WPS_USER

wget -q --user=$WPS_USER --ask-password -O- "www.brockmann-consult.de/bc-wps/wps/calvalus?Service=WPS&Request=DescribeProcess&Version=1.0.0&Identifier=${PROCESS_ID}"
