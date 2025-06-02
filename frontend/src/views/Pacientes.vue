<template>
  <div>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <div class="container">
        <router-link to="/" class="nav-link">Início</router-link>
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav ms-auto">
            <li class="nav-item">
              <router-link to="/pacientes/cadastrar" class="nav-link">
                Cadastrar Paciente
              </router-link>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <div class="container mt-4">
      <h4 class="mb-4">Lista de Pacientes</h4>

      <input
        type="text"
        class="form-control mb-3"
        v-model="termoBusca"
        placeholder="Pesquisar"
      />

      <ul class="list-group">
        <li
          v-for="paciente in pacientesFiltrados"
          :key="paciente.id"
          class="list-group-item"
        >
          <div
            @click="toggleExpandir(paciente.id)"
            class="d-flex justify-content-between align-items-center pointer">
            <p class="mb-0"><strong>Nome:</strong> {{ paciente.nome }}</p>
            <span>{{ pacienteExpandido === paciente.id ? "▲" : "▼" }}</span>
          </div>

          <div
            v-if="pacienteExpandido === paciente.id && detalhesPaciente[paciente.id]"
            class="mt-3 ms-3"
          >
            <p><strong>Email:</strong> {{ detalhesPaciente[paciente.id].email }}</p>
            <p><strong>Telefone:</strong> {{ detalhesPaciente[paciente.id].telefone }}</p>
            <p><strong>CPF:</strong> {{ detalhesPaciente[paciente.id].cpf }}</p>
            <p><strong>Endereço:</strong>
              {{ detalhesPaciente[paciente.id].endereco.logradouro }},
              {{ detalhesPaciente[paciente.id].endereco.numero }},
              {{ detalhesPaciente[paciente.id].endereco.bairro }},
              {{ detalhesPaciente[paciente.id].endereco.cidade }}/
              {{ detalhesPaciente[paciente.id].endereco.uf }} -
              CEP: {{ detalhesPaciente[paciente.id].endereco.cep }}
            </p>
            <p v-if="detalhesPaciente[paciente.id].endereco.complemento">
              <strong>Complemento:</strong> {{ detalhesPaciente[paciente.id].endereco.complemento }}
            </p>

            <div v-if="consultasPorPaciente[paciente.id]?.length">
              <h5 class="mt-3">Consultas</h5>
              <ul>
                <li
                  v-for="consulta in consultasPorPaciente[paciente.id]"
                  :key="consulta.id"
                  class="mt-3"
                >
                  Data: {{ formatarData(consulta.data) }}<br />
                  Médico: {{ consulta.nomeMedico }} 
                </li>
              </ul>
            </div>
            <div v-else>
              <p><em>Sem consultas agendadas.</em></p>
            </div>

            <!-- Botões de ação -->
            <div class="mt-3 text-end">
              <button
                @click.stop="editarPaciente(paciente)"
                class="btn btn-sm btn-outline-primary mx-2"
              >
                Editar
              </button>
              <button
                @click.stop="excluirPaciente(paciente.id)"
                class="btn btn-sm btn-outline-danger"
              >
                Excluir
              </button>
            </div>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import api from "@/services/api";

export default {
  data() {
    return {
      pacientes: [],
      termoBusca: "",
      pacienteExpandido: null,
      detalhesPaciente: {},
      consultasPorPaciente: {},
    };
  },
  mounted() {
    this.buscarPacientes();
  },
  methods: {
    async buscarPacientes() {
      try {
        const response = await api.get("/pacientes");
        this.pacientes = response.data.content || response.data;
      } catch (error) {
        console.error("Erro ao buscar pacientes:", error);
      }
    },
    async buscarDetalhesPaciente(id) {
      try {
        const response = await api.get(`/pacientes/${id}`);
        this.detalhesPaciente[id] = response.data;
      } catch (error) {
        console.error("Erro ao buscar detalhes:", error);
      }
    },
    async buscarConsultasDoPaciente(id) {
      try {
        const response = await api.get(`/consultas/paciente/${id}`);
        this.consultasPorPaciente[id] = response.data;
      } catch (error) {
        console.error("Erro ao buscar consultas:", error);
        this.consultasPorPaciente[id] = [];
      }
    },
    async toggleExpandir(id) {
      if (this.pacienteExpandido === id) {
        this.pacienteExpandido = null;
      } else {
        this.pacienteExpandido = id;

        if (!this.detalhesPaciente[id]) {
          await this.buscarDetalhesPaciente(id);
        }

        await this.buscarConsultasDoPaciente(id);
      }
    },
    formatarData(data) {
      const d = new Date(data);
      return d.toLocaleDateString("pt-BR", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
      });
    },
    editarPaciente(paciente) {
      this.$router.push(`/pacientes/editar/${paciente.id}`);
    },
    async excluirPaciente(id) {
      if (!confirm("Tem certeza que deseja excluir este paciente?")) return;

      try {
        await api.delete(`/pacientes/${id}`);
        this.pacientes = this.pacientes.filter((p) => p.id !== id);
        delete this.detalhesPaciente[id];
        delete this.consultasPorPaciente[id];
        this.pacienteExpandido = null;
        alert("Paciente excluído com sucesso!");
      } catch (error) {
        console.error("Erro ao excluir paciente:", error);
        alert("Erro ao excluir paciente.");
      }
    },
  },
  computed: {
    pacientesFiltrados() {
      const termo = this.termoBusca.toLowerCase();
      return this.pacientes.filter((p) => p.nome.toLowerCase().includes(termo));
    },
  }
};
</script>
