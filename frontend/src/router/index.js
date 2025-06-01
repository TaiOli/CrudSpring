import { createRouter, createWebHistory } from "vue-router";
import Home from "../views/Home.vue";
import Medicos from "../views/Medicos.vue";
import Pacientes from "../views/Pacientes.vue";
import Consultas from "../views/Consultas.vue";
import CadastroMedico from "../views/CadastroMedico.vue";

const routes = [
	{ path: "/", component: Home },
	{ path: "/medicos", component: Medicos },
	{ path: "/pacientes", component: Pacientes },
	{ path: "/consultas", component: Consultas },
	{ path: "/medicos/cadastrar", component: CadastroMedico },
	{ path: "/medicos/editar/:id", component: CadastroMedico },
];

const router = createRouter({
	history: createWebHistory(),
	routes,
});

export default router;
