<template>
  <div>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <div class="container">
        <router-link to="/" class="nav-link">Inicio</router-link>
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
          aria-controls="navbarNav"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav ms-auto">
            <li class="nav-item">
              <router-link to="/medicos/cadastrar" class="nav-link">
                Cadastrar Médico
              </router-link>
            </li>
          </ul>
        </div>
      </div>
    </nav>

    <div class="container mt-4">
      <h4 class="mb-4">Lista de Médicos</h4>

      <div class="mb-3">
        <input
          type="text"
          class="form-control"
          v-model="termoBusca"
          placeholder="Pesquisar"
        />
      </div>

      <ul class="list-group">
        <li
          class="list-group-item"
          v-for="medico in medicosFiltrados"
          :key="medico.id"
        >
          <div @click="toggleExpandir(medico.id)" class="d-flex justify-content-between align-items-center" style="cursor: pointer;">
            <div>
              <strong>{{ medico.nome }}</strong><br />
              <small class="text-muted">{{ medico.especialidade }}</small>
            </div>
            <div>
              <span v-if="medicoExpandido === medico.id">▲</span>
              <span v-else>▼</span>
            </div>
          </div>

          <div v-if="medicoExpandido === medico.id" class="mt-2 text-end">
            <button @click="editarMedico(medico)" class="btn btn-sm btn-outline-primary mx-2">
              Editar
            </button>
             <button @click="excluirMedico(medico.id)" class="btn btn-sm btn-outline-danger">
                Excluir
            </button>
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
			medicos: [],
			termoBusca: "",
			medicoExpandido: null,
		};
	},
	mounted() {
		this.buscarMedicos();
	},
	methods: {
		async buscarMedicos() {
			try {
				const response = await api.get("/medicos");
				const lista = response.data.content;
				this.medicos = lista.sort((a, b) => a.nome.localeCompare(b.nome));
			} catch (error) {
				console.error("Erro ao buscar médicos:", error);
			}
		},
		toggleExpandir(id) {
			this.medicoExpandido = this.medicoExpandido === id ? null : id;
		},
		editarMedico(medico) {
			this.$router.push(`/medicos/editar/${medico.id}`);
		},
		async excluirMedico(id) {
			if (!confirm("Tem certeza que deseja excluir este médico?")) return;

			try {
				await api.delete(`/medicos/${id}`);
				this.medicos = this.medicos.filter((medico) => medico.id !== id);
				if (this.medicoExpandido === id) this.medicoExpandido = null;
				alert("Médico excluído com sucesso!");
			} catch (error) {
				console.error("Erro ao excluir médico:", error);
				alert("Erro ao excluir médico.");
			}
		},
	},
	computed: {
		medicosFiltrados() {
			if (!this.termoBusca) return this.medicos;
			const busca = this.termoBusca.toLowerCase();
			return this.medicos.filter((medico) =>
				medico.nome.toLowerCase().includes(busca),
			);
		},
	},
};
</script>
