
import { useState, type FormEvent } from "react";
import type { LoginFormData } from "../../interfaces/ILogin";
import "./Login.css";

function Login() {
  const [formData, setFormData] = useState<LoginFormData>({
    email: "",
    senha: "",
  });

  const handleChange = (
    event: React.ChangeEvent<HTMLInputElement>
  ) => {
    const { name, value } = event.target;

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    console.log("Dados do login:", formData);
  };

  return (
    <main className="login-container">
      <form className="login-form" onSubmit={handleSubmit}>
        <h1>Bem-vindo!</h1>
        <p>Entre com suas credenciais para continuar.</p>

        <div className="form-group">
          <label htmlFor="email">E-mail</label>

          <input
            type="email"
            id="email"
            name="email"
            placeholder="Digite seu e-mail"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="senha">Senha</label>

          <input
            type="password"
            id="senha"
            name="senha"
            placeholder="Digite sua senha"
            value={formData.senha}
            onChange={handleChange}
            required
          />
        </div>

        <button type="submit">
          Entrar
        </button>
      </form>
    </main>
  );
}

export default Login;
