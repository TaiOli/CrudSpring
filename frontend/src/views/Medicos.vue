<template>
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
