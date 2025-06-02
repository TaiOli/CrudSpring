<template>
  <div class="container mt-4">
    <h4 class="mb-5 titleConsulta">Agendar Consulta</h4>
    <form @submit.prevent="submitForm">
      
      <div class="mb-3 position-relative">
        <label for="paciente" class="form-label">Paciente</label>
        <input
          type="text"
          id="paciente"
          class="form-control"
          v-model="buscaPaciente"
          @input="buscarPacientes"
          autocomplete="off"
          placeholder="Digite o nome do paciente"
          @blur="() => setTimeout(() => sugestoesPacientes = [], 200)" 
        />
        <ul
          v-if="sugestoesPacientes.length > 0"
          class="list-group position-absolute w-100 selectPaciente">
          <li
            v-for="paciente in sugestoesPacientes"
            :key="paciente.id"
            class="list-group-item list-group-item-action pointer"
            @click="selecionarPaciente(paciente)">
            {{ paciente.nome }}
          </li>
        </ul>
      </div>

      <div class="mb-3">
        <label for="especialidade" class="form-label">Especialidade</label>
        <select
          id="especialidade"
          class="form-select"
          v-model="form.especialidade"
          @change="limparMedico"
          required
        >
          <option value="">Selecione</option>
          <option v-for="esp in especialidades" :key="esp" :value="esp">{{ esp }}</option>
        </select>
      </div>

      <div class="mb-3 position-relative">
        <label for="medico" class="form-label">Médico</label>
        <input
          type="text"
          id="medico"
          class="form-control"
          v-model="buscaMedico"
          @input="buscarMedicos"
          :disabled="!form.especialidade"
          autocomplete="off"
          placeholder="Digite o nome do médico"
          @blur="() => setTimeout(() => sugestoesMedicos = [], 200)" 
        />
        <ul
          v-if="sugestoesMedicos.length > 0"
          class="list-group position-absolute w-100 selectMedico">
          <li
            v-for="medico in sugestoesMedicos"
            :key="medico.id"
            class="list-group-item list-group-item-action pointer"
            @click="selecionarMedico(medico)">
            {{ medico.nome }}
          </li>
        </ul>
      </div>

      <div class="mb-3">
        <label for="data" class="form-label">Data e hora</label>
        <input
          type="datetime-local"
          id="data"
          class="form-control"
          v-model="form.data"
          required
        />
      </div>

      <button type="submit" class="btn btn-Agendar">Agendar</button>
    </form>
  </div>
</template>

<script>
import api from "@/services/api";

export default {
  data() {
    return {
      buscaPaciente: "",
      sugestoesPacientes: [],
      buscaMedico: "",
      sugestoesMedicos: [],
      especialidades: ["CARDIOLOGIA", "DERMATOLOGIA", "ORTOPEDIA", "GINECOLOGIA", "PEDIATRIA"],

      form: {
        idPaciente: null,
        idMedico: null,
        data: "",
        especialidade: "",
      },
    };
  },
  methods: {
    buscarPacientes() {
      if (this.buscaPaciente.length < 1) {
        this.sugestoesPacientes = [];
        this.form.idPaciente = null;
        return;
      }
      api
        .get("/pacientes", { params: { nome: this.buscaPaciente } })
        .then((response) => {
          this.sugestoesPacientes = response.data.content || [];
        })
        .catch(() => {
          this.sugestoesPacientes = [];
        });
    },
    selecionarPaciente(paciente) {
      this.form.idPaciente = paciente.id;
      this.buscaPaciente = paciente.nome;
      this.sugestoesPacientes = [];
    },

    limparMedico() {
 
      this.buscaMedico = "";
      this.form.idMedico = null;
      this.sugestoesMedicos = [];
    },

    buscarMedicos() {
      if (!this.form.especialidade) {

        this.sugestoesMedicos = [];
        this.form.idMedico = null;
        return;
      }
      if (this.buscaMedico.length < 1) {
        this.sugestoesMedicos = [];
        this.form.idMedico = null;
        return;
      }
      api
        .get("/medicos", {
          params: {
            nome: this.buscaMedico,
            especialidade: this.form.especialidade,
          },
        })
        .then((response) => {
          this.sugestoesMedicos = response.data.content || [];
        })
        .catch(() => {
          this.sugestoesMedicos = [];
        });
    },
    selecionarMedico(medico) {
      this.form.idMedico = medico.id;
      this.buscaMedico = medico.nome;
      this.sugestoesMedicos = [];
    },

    submitForm() {
      if (!this.form.idPaciente) {
        alert("Por favor, selecione um paciente válido.");
        return;
      }
      if (!this.form.data) {
        alert("Por favor, informe a data da consulta.");
        return;
      }
      if (!this.form.especialidade) {
        alert("Por favor, selecione a especialidade.");
        return;
      }

      api
        .post("/consultas", {
          idPaciente: this.form.idPaciente,
          idMedico: this.form.idMedico,
          data: this.form.data,
          especialidade: this.form.especialidade,
        })
        .then(() => {
          alert("Consulta agendada com sucesso!");

          this.buscaPaciente = "";
          this.form.idPaciente = null;
          this.buscaMedico = "";
          this.form.idMedico = null;
          this.form.data = "";
          this.form.especialidade = "";
          this.sugestoesPacientes = [];
          this.sugestoesMedicos = [];
        })
        .catch((error) => {
          alert("Erro ao agendar consulta: " + (error.response?.data?.message || error.message));
        });
    },
  },
};
</script>