<template>
  <div class="container mt-5">
    <div class="text-secondary text-start mb-4">Escolha uma seção para iniciar:</div>

    <div class="d-flex flex-column gap-3">
      <router-link to="/medicos" class="w-100">
        <button class="btn btn-outline-primary w-100 text-start">Médicos</button>
      </router-link>
      <router-link to="/pacientes" class="w-100">
        <button class="btn btn-outline-success w-100 text-start">Pacientes</button>
      </router-link>
      <router-link to="/consultas" class="w-100">
        <button class="btn btn-outline-warning w-100 text-start">Consultas</button>
      </router-link>
    </div>
  </div>
</template>
