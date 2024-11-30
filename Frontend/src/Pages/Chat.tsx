import React, { useState } from "react";
import openaiClient from "./openaiClient";

interface Message {
  role: "user" | "assistant";
  content: string;
}

const Chat: React.FC = () => {
  const [messages, setMessages] = useState<Message[]>([
    { role: "assistant", content: "¡Hola! Soy tu asistente IA. ¿En qué puedo ayudarte hoy?" },
  ]);
  const [input, setInput] = useState("");

  const sendMessage = async () => {
    if (!input.trim()) return;

    const newMessages = [...messages, { role: "user", content: input }];
    setMessages(newMessages);
    setInput("");

    try {
      const response = await openaiClient.chat.completions.create({
        messages: newMessages,
        model: "gpt-4o-mini",
      });

      const aiMessage = response.choices[0].message.content;
      setMessages([...newMessages, { role: "assistant", content: aiMessage }]);
    } catch (error) {
      console.error("Error al interactuar con la API:", error);
      setMessages([
        ...newMessages,
        { role: "assistant", content: "Lo siento, ocurrió un error al procesar tu solicitud." },
      ]);
    }
  };

  return (
    <div>
      <div style={{ maxHeight: "400px", overflowY: "auto" }}>
        {messages.map((msg, index) => (
          <div key={index} style={{ margin: "10px 0", textAlign: msg.role === "user" ? "right" : "left" }}>
            <strong>{msg.role === "user" ? "Tú" : "IA"}:</strong> {msg.content}
          </div>
        ))}
      </div>
      <input
        type="text"
        value={input}
        onChange={(e) => setInput(e.target.value)}
        placeholder="Escribe tu mensaje..."
        onKeyDown={(e) => e.key === "Enter" && sendMessage()}
        style={{ width: "calc(100% - 20px)", margin: "10px", padding: "10px" }}
      />
      <button onClick={sendMessage} style={{ padding: "10px 20px" }}>
        Enviar
      </button>
    </div>
  );
};

export default Chat;
