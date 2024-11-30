import OpenAI from "openai";

const apiKey = process.env.REACT_APP_OPENAI_API_KEY;

if (!apiKey) {
  throw new Error("La clave de API de OpenAI no está configurada.");
}

const client = new OpenAI({
  apiKey,
});

export default client;