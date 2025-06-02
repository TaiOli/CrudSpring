<template>
  <div class="container d-flex flex-column align-items-center justify-content-center vh-100">
    <h5 class="text-primary fw-bold">Gerenciando sua clínica</h5>

    <p class="mt-4 mb-3 text-secondary">Escolha qual seção deseja iniciar:</p>

    <div class="d-flex flex-column gap-3 w-100 btnAgenda">
      <router-link to="/medicos">
        <button class="btn btn-primary w-100 py-3 d-flex align-items-center justify-content-center gap-2">
          <i class="bi bi-person-badge"></i>
          Médicos(as)
        </button>
      </router-link>

      <router-link to="/pacientes">
        <button class="btn btn-primary w-100 py-3 d-flex align-items-center justify-content-center gap-2">
          <i class="bi bi-person"></i>
          Pacientes
        </button>
      </router-link>

      <router-link to="/consulta">
        <button class="btn btn-primary w-100 py-3 d-flex align-items-center justify-content-center gap-2">
          <i class="bi bi-calendar-event"></i>
          Consultas
        </button>
      </router-link>
    </div>
  </div>
</template>
