<template>
  <div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h4 class="mb-0 titleCadPaciente">{{ isEditando ? 'Editar Paciente' : 'Novo Perfil' }}</h4>
      <router-link to="/pacientes" class="btn btn-Voltar">Voltar</router-link>
    </div>

    <h5 class="mt-5 titleText">Dados Pessoais</h5>

    <div class="mb-3">
      <label for="nome" class="form-label titleText mt-3">Nome Completo</label>
      <input type="text" id="nome" class="form-control" v-model="form.nome" />
    </div>

    <div class="row mb-3">
      <div class="col-md-6">
        <label for="cpf" class="form-label titleText">CPF</label>
        <input type="text" id="cpf" class="form-control" v-model="form.cpf" />
      </div>
      <div class="col-md-6">
        <label for="telefone" class="form-label titleText">Telefone</label>
        <input type="text" id="telefone" class="form-control" v-model="form.telefone" />
      </div>
    </div>

    <div class="mb-3">
      <label for="email" class="form-label titleText">E-mail</label>
      <input type="email" id="email" class="form-control" v-model="form.email" />
    </div>

    <h5 class="mt-5 titleText">Endereço</h5>

    <div class="mb-3">
      <label for="cep" class="form-label titleText mt-3">CEP</label>
      <input type="text" id="cep" class="form-control" v-model="form.endereco.cep" />
    </div>

    <div class="mb-3">
      <label for="logradouro" class="form-label titleText">Logradouro</label>
      <input type="text" id="logradouro" class="form-control" v-model="form.endereco.logradouro" />
    </div>

    <div class="row mb-3">
      <div class="col-md-6">
        <label for="numero" class="form-label titleText">Número</label>
        <input type="text" id="numero" class="form-control" v-model="form.endereco.numero" />
      </div>
      <div class="col-md-6">
        <label for="complemento" class="form-label titleText">Complemento</label>
        <input type="text" id="complemento" class="form-control" v-model="form.endereco.complemento" />
      </div>
    </div>

    <div class="mb-3">
      <label for="bairro" class="form-label titleText">Bairro</label>
      <input type="text" id="bairro" class="form-control" v-model="form.endereco.bairro" />
    </div>

    <div class="row mb-3">
      <div class="col-md-8">
        <label for="cidade" class="form-label titleText">Cidade</label>
        <input type="text" id="cidade" class="form-control" v-model="form.endereco.cidade" />
      </div>
      <div class="col-md-4">
        <label for="uf" class="form-label titleText">UF</label>
        <input type="text" id="uf" class="form-control" maxlength="2" v-model="form.endereco.uf" />
      </div>
    </div>

    <button class="btn btn-Salvar mt-3" @click="salvarPaciente">
      {{ isEditando ? 'Atualizar' : 'Cadastrar' }}
    </button>
  </div>
</template>

<script>
import api from "@/services/api";

export default {
  data() {
    return {
      form: {
        nome: "",
        email: "",
        cpf: "",
        telefone: "",
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
      this.carregarPaciente(id);
    }
  },
  methods: {
    async carregarPaciente(id) {
      try {
        const response = await api.get(`/pacientes/${id}`);
        this.form = response.data;
      } catch (error) {
        console.error("Erro ao carregar paciente:", error);
        alert("Não foi possível carregar os dados do paciente.");
        this.$router.push("/pacientes");
      }
    },
    async salvarPaciente() {
      try {
        if (this.isEditando) {
          await api.put(`/pacientes/${this.$route.params.id}`, this.form);
          alert("Paciente atualizado com sucesso!");
        } else {
          await api.post("/pacientes", this.form);
          alert("Paciente cadastrado com sucesso!");
          this.limparFormulario();
        }
        this.$router.push("/pacientes");
      } catch (error) {
        console.error("Erro ao salvar paciente:", error);
        alert("Erro ao salvar paciente. Verifique os dados ou o console.");
      }
    },
    limparFormulario() {
      this.form = {
        nome: "",
        email: "",
        cpf: "",
        telefone: "",
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
