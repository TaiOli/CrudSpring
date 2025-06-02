import { createRouter, createWebHistory } from "vue-router";
import Home from "../views/Home.vue";
import Medicos from "../views/Medicos.vue";
import Pacientes from "../views/Pacientes.vue";
import AgendaConsulta from "../views/AgendaConsulta.vue";
import CadastroMedico from "../views/CadastroMedico.vue";
import CadastroPaciente from "../views/CadastroPaciente.vue";

const routes = [
	{ path: "/", component: Home },
	{ path: "/medicos", component: Medicos },
	{ path: "/pacientes", component: Pacientes },
	{ path: "/consulta", component: AgendaConsulta },
	{ path: "/medicos/cadastrar", component: CadastroMedico },
	{ path: "/medicos/editar/:id", component: CadastroMedico },
	{ path: "/pacientes/cadastrar", component: CadastroPaciente },
	{ path: "/pacientes/editar/:id", component: CadastroPaciente },
];

const router = createRouter({
	history: createWebHistory(),
	routes,
});

export default router;
