#!/bin/sh

mkdir data
java -cp target/word2vec-example-1.0-SNAPSHOT-jar-with-dependencies.jar wordseg.Text2Word -i data/news_tensite_xml.smarty.dat -o data/news_word.txt
time ./word2vec/bin/word2vec -train data/news_word.txt -output data/vectors.bin -cbow 1 -size 200 -window 8 -negative 25 -hs 0 -sample 1e-4 -threads 20 -binary 0 -iter 15 
./word2vec/bin/distance data/vectors.bin
