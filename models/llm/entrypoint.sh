#!/bin/sh

export OLLAMA_HOST=0.0.0.0:8080
export OLLAMA_ORIGINS="*"
export OLLAMA_MODELS=/models
ollama serve &

sleep 2
ollama pull gemma3:1b
wait %1