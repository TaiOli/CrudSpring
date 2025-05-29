import { createRouter, createWebHistory } from "vue-router";
import Home from "../views/Home.vue";
import Medicos from "../views/Medicos.vue";
import Pacientes from "../views/Pacientes.vue";
import Consultas from "../views/Consultas.vue";

const routes = [
	{ path: "/", component: Home },
	{ path: "/medicos", component: Medicos },
	{ path: "/pacientes", component: Pacientes },
	{ path: "/consultas", component: Consultas },
];

const router = createRouter({
	history: createWebHistory(),
	routes,
});

export default router;
