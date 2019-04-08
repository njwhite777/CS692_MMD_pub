#!/bin/bash

TESTNAME=$1
RENAMETEST=$2

mv ./test/$TESTNAME ./test/$RENAMETEST

mv ./test/$RENAMETEST/input/$TESTNAME.dma ./test/$RENAMETEST/input/$RENAMETEST.dma
mv ./test/$RENAMETEST/input/$TESTNAME.dma_fmt ./test/$RENAMETEST/input/$RENAMETEST.dma_fmt

