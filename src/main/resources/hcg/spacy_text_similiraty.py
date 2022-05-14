import os

import spacy


def tokenize(function_text):
    return nlp(function_text)


def vectorize(tokens_a, tokens_b):
    tokens_a.similarity(tokens_b)


def readCalling():
    dir = os.path.join(os.getcwd(), "\src\main\resources\static\working-dir\calling.txt")
    with open(dir) as f:
        return f.readlines()


def readCalled():
    dir = os.path.join(os.getcwd(), "\src\main\resources\static\working-dir\called.txt")
    with open(dir) as f:
        return f.readlines()


def remove_stopwords(doc):
    result = []
    for token in doc:
        if token.text in nlp.Defaults.stop_words:
            continue
        result.append(token.text)
    return " ".join(result)


if __name__ == '__main__':
    nlp = spacy.load('en_core_web_md')
    function_calling = readCalling()
    function_called = readCalled()
    doc_a = tokenize(function_calling)
    doc_b = tokenize(function_called)
    vectorize(doc_a, doc_b)
