<template>
  <div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h4 class="mb-0">{{ isEditando ? 'Editar Perfil' : 'Novo Perfil' }}</h4>
      <router-link to="/medicos" class="btn btn-secondary">Voltar</router-link>
    </div>

    <h4 class="mt-5">Profissional</h4>

    <div class="mb-3">
      <label for="nome" class="form-label">Nome Completo</label>
      <input type="text" id="nome" class="form-control" v-model="form.nome" />
    </div>

    <div class="row mb-3">
      <div class="col-md-6">
        <label for="especialidade" class="form-label">Especialidade</label>
        <select id="especialidade" class="form-select" v-model="form.especialidade">
          <option value="" disabled>Especialidade</option>
          <option v-for="esp in especialidades" :key="esp" :value="esp">{{ esp }}</option>
        </select>
      </div>
      <div class="col-md-6">
        <label for="crm" class="form-label">CRM</label>
        <input type="text" id="crm" class="form-control" v-model="form.crm" />
      </div>
    </div>

    <h4 class="mt-5">Contatos</h4>

    <div class="mb-3">
      <label for="email" class="form-label">E-mail</label>
      <input type="email" id="email" class="form-control" v-model="form.email" />
    </div>

    <div class="mb-3">
      <label for="telefone" class="form-label">Telefone</label>
      <input type="text" id="telefone" class="form-control" v-model="form.telefone" />
    </div>

    <h4 class="mt-5">Endereço Profissional</h4>

    <div class="mb-3">
      <label for="cep" class="form-label">CEP</label>
      <input type="text" id="cep" class="form-control" v-model="form.endereco.cep" />
    </div>

    <div class="mb-3">
      <label for="logradouro" class="form-label">Logradouro</label>
      <input type="text" id="logradouro" class="form-control" v-model="form.endereco.logradouro" />
    </div>

    <div class="row mb-3">
      <div class="col-md-6">
        <label for="numero" class="form-label">Número</label>
        <input type="text" id="numero" class="form-control" v-model="form.endereco.numero" />
      </div>
      <div class="col-md-6">
        <label for="complemento" class="form-label">Complemento</label>
        <input type="text" id="complemento" class="form-control" v-model="form.endereco.complemento" />
      </div>
    </div>

    <div class="mb-3">
      <label for="bairro" class="form-label">Bairro</label>
      <input type="text" id="bairro" class="form-control" v-model="form.endereco.bairro" />
    </div>

    <div class="row mb-3">
      <div class="col-md-8">
        <label for="cidade" class="form-label">Cidade</label>
        <input type="text" id="cidade" class="form-control" v-model="form.endereco.cidade" />
      </div>
      <div class="col-md-4">
        <label for="uf" class="form-label">UF</label>
        <input type="text" id="uf" class="form-control" maxlength="2" v-model="form.endereco.uf" />
      </div>
    </div>

    <button class="btn btn-primary mt-3" @click="salvarMedico">
      {{ isEditando ? 'Atualizar' : 'Cadastrar' }}
    </button>
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
			},
			isEditando: false,
		};
	},
	created() {
		const id = this.$route.params.id;
		if (id) {
			this.isEditando = true;
			this.carregarMedico(id);
		}
	},
	methods: {
		async carregarMedico(id) {
			try {
				const response = await api.get(`/medicos/${id}`);
				this.form = response.data; // assumindo que a API retorna no mesmo formato do form
			} catch (error) {
				console.error("Erro ao carregar médico:", error);
				alert("Não foi possível carregar os dados do médico.");
				this.$router.push("/medicos");
			}
		},
		async salvarMedico() {
			try {
				if (this.isEditando) {
					// Update
					await api.put(`/medicos/${this.$route.params.id}`, this.form);
					alert("Médico atualizado com sucesso!");
				} else {
					// Create
					await api.post("/medicos", this.form);
					alert("Médico cadastrado com sucesso!");
					this.limparFormulario();
				}
				this.$router.push("/medicos");
			} catch (error) {
				console.error("Erro ao salvar médico:", error);
				alert("Erro ao salvar médico. Verifique os dados ou o console.");
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