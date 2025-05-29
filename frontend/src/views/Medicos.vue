<template>
  <div>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <div class="container">
        <a class="navbar-brand" href="#">Clinica</a>
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
             <router-link to="/medicos/cadastrar" class="text-decoration-none">
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
          class="list-group-item d-flex justify-content-between align-items-center"
          v-for="medico in medicosFiltrados"
          :key="medico.id"
        >
          <div>
            <strong>{{ medico.nome }}</strong>
            <br />
            <small class="text-muted">{{ medico.especialidade }}</small>
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
