#!/bin/bash

TESTNAME=$1
mkdir -p ./test/$TESTNAME/input ./test/$TESTNAME/output
touch ./test/$TESTNAME/input/$TESTNAME.dma
touch ./test/$TESTNAME/input/$TESTNAME.dma_fmt
touch ./test/$TESTNAME/output/pass.txt
