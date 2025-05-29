<template>
  <div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h4 class="mb-0">Novo Perfil</h4>
      <router-link to="/medicos" class="btn btn-secondary">Voltar</router-link>
    </div>

    <h4 class="mt-5">Profissional</h4>

    <div class="mb-3">
      <label for="nome" class="form-label mt-3">Nome Completo</label>
      <input
        type="text"
        id="nome"
        class="form-control"
        v-model="form.nome"
        placeholder="Nome Completo"
      />
    </div>

    <div class="row mb-3">
      <div class="col-md-6">
        <label for="especialidade" class="form-label">Especialidade</label>
        <select
          id="especialidade"
          class="form-select"
          v-model="form.especialidade"
        >
          <option value="" disabled>Especialidade</option>
          <option v-for="esp in especialidades" :key="esp" :value="esp">
            {{ esp }}
          </option>
        </select>
      </div>

      <div class="col-md-6">
        <label for="crm" class="form-label">CRM</label>
        <input
          type="text"
          id="crm"
          class="form-control"
          v-model="form.crm"
          placeholder="CRM"
        />
      </div>
    </div>

    <h4 class="mt-5">Contatos</h4>

    <div class="mb-3">
      <label for="email" class="form-label mt-3">E-mail</label>
      <input
        type="email"
        id="email"
        class="form-control"
        v-model="form.email"
        placeholder="E-mail"
      />
    </div>

    <div class="mb-3">
    <label for="telefone" class="form-label">Telefone</label>
    <input
        type="text"
        id="telefone"
        class="form-control"
        v-model="form.telefone"
        placeholder="Telefone"
    />
    </div>

    <h4 class="mt-5">Endereço Profissional</h4>

    <div class="mb-3">
      <label for="cep" class="form-label mt-3">CEP</label>
      <input
        type="text"
        id="cep"
        class="form-control"
        v-model="form.endereco.cep"
        placeholder="CEP"
      />
    </div>

    <div class="mb-3">
      <label for="logradouro" class="form-label">Logradouro</label>
      <input
        type="text"
        id="logradouro"
        class="form-control"
        v-model="form.endereco.logradouro"
        placeholder="Logradouro"
      />
    </div>

    <div class="row mb-3">
      <div class="col-md-6">
        <label for="numero" class="form-label">Número</label>
        <input
          type="text"
          id="numero"
          class="form-control"
          v-model="form.endereco.numero"
          placeholder="Número"
        />
      </div>

      <div class="col-md-6">
        <label for="complemento" class="form-label">Complemento</label>
        <input
          type="text"
          id="complemento"
          class="form-control"
          v-model="form.endereco.complemento"
          placeholder="Complemento"
        />
      </div>
    </div>

    <div class="mb-3">
      <label for="bairro" class="form-label">Bairro</label>
      <input
        type="text"
        id="bairro"
        class="form-control"
        v-model="form.endereco.bairro"
        placeholder="Bairro"
      />
    </div>

    <div class="row mb-3">
      <div class="col-md-8">
        <label for="cidade" class="form-label">Cidade</label>
        <input
          type="text"
          id="cidade"
          class="form-control"
          v-model="form.endereco.cidade"
          placeholder="Cidade"
        />
      </div>

      <div class="col-md-4">
        <label for="uf" class="form-label">UF</label>
        <input
          type="text"
          id="uf"
          class="form-control"
          v-model="form.endereco.uf"
          maxlength="2"
          placeholder="UF"
        />
      </div>
    </div>

    <button class="btn btn-primary mt-3" @click="cadastrarMedico">Cadastrar</button>
  </div>
</template>

<script>
import api from "@/services/api";

export default {
	data() {
		return {
			especialidades: [
				"ORTOPEDIA",
				"CARDIOLOGIA",
				"GINECOLOGIA",
				"DERMATOLOGIA",
			],
			form: {
				nome: "",
				email: "",
				crm: "",
				especialidade: "",
				endereco: {
					cep: "",
					logradouro: "",
					numero: "",
					complemento: "",
					bairro: "",
					cidade: "",
					uf: "",
				},
			},
		};
	},
	methods: {
		async cadastrarMedico() {
			try {
				const dadosParaEnviar = {
					nome: this.form.nome,
					email: this.form.email,
					telefone: this.form.telefone,
					crm: this.form.crm,
					especialidade: this.form.especialidade,
					endereco: {
						cep: this.form.endereco.cep,
						logradouro: this.form.endereco.logradouro,
						numero: this.form.endereco.numero,
						complemento: this.form.endereco.complemento,
						bairro: this.form.endereco.bairro,
						cidade: this.form.endereco.cidade,
						uf: this.form.endereco.uf,
					},
				};

				await api.post("/medicos", dadosParaEnviar);

				alert("Médico cadastrado com sucesso!");

				this.limparFormulario();

				this.$router.push("/medicos");
			} catch (error) {
				console.error("Erro ao cadastrar médico:", error);
				alert("Erro ao cadastrar médico. Veja o console para detalhes.");
			}
		},
		limparFormulario() {
			this.form = {
				nome: "",
				email: "",
				telefone: "",
				crm: "",
				especialidade: "",
				endereco: {
					cep: "",
					logradouro: "",
					numero: "",
					complemento: "",
					bairro: "",
					cidade: "",
					uf: "",
				},
			};
		},
	},
};
</script>