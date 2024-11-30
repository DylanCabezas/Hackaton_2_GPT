import React, { useState } from "react";

const ChatPage: React.FC = () => {
  const [messages, setMessages] = useState<string[]>([]);
  const [input, setInput] = useState<string>("");

  const handleSendMessage = () => {
    if (input.trim()) {
      setMessages([...messages, input]);
      setInput("");
      // Aquí es donde llamarías a la API de IA para obtener una respuesta
    }
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-900">
      <div className="max-w-md w-full bg-white p-6 rounded-lg shadow-lg space-y-4">
        <h2 className="text-2xl font-bold text-center text-gray-800">Chat IA</h2>
        <div className="space-y-2">
          {messages.map((message, index) => (
            <div key={index} className="p-2 bg-gray-200 rounded-lg">
              {message}
            </div>
          ))}
        </div>
        <div className="flex space-x-2">
          <input
            type="text"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            className="flex-1 p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
          <button
            onClick={handleSendMessage}
            className="bg-blue-600 text-white p-3 rounded-lg hover:bg-blue-500 focus:outline-none"
          >
            Enviar
          </button>
        </div>
      </div>
    </div>
  );
};

export default ChatPage;