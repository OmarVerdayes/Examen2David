<template>
  <div class="container" style="width: 100vw;">
    <div class="scroll-hide">
      <ModalRegistroVue />
      <br />
    </div>

    <div class="container">
      <div class="container">
        <!-- --------------------------------AQUI VA TODO LA VISTA DE LOS FILTROS---------------------------->
        <br />
        <div>
          Filtro:
          <b-form-select v-model="selected" :options="options"></b-form-select>
        </div>
        <br />

        <div v-if="selected === 'vacio'">
          {{ obtenerLibros() }}
        </div>

        <div v-if="selected === 'Nombre'">
          <form @submit.prevent="obtenerLibrosNombre(nombreFiltro)">
            <b-form-group label="Nombre del libro:">
              <b-form-input v-model="nombreFiltro" required />
              <b-button type="submit" variant="primary">Buscar</b-button>
            </b-form-group>
          </form>
        </div>

        <div v-if="selected === 'Autor'">
          <form @submit.prevent="obtenerLibrosAutor(nombreFiltro)">
            <b-form-group label="Nombre del auttor:">
              <b-form-input v-model="nombreFiltro" required />
              <b-button type="submit" variant="primary">Buscar</b-button>
            </b-form-group>
          </form>
        </div>
        <div v-if="selected === 'Fechas'">
          <form @submit.prevent="obtenerLibrosAnios(anioI, anioF)">
            <b-form-group label="Nombre del libro:">
              <b-form-input
                v-model="anioI"
                type="number"
                max="2024"
                min="1"
                required
              />
              <b-form-input
                v-model="anioF"
                type="number"
                max="2024"
                min="1"
                required
              />
              <b-button type="submit" variant="primary">Buscar</b-button>
            </b-form-group>
          </form>
        </div>
        <br />
        <div></div>
        <!-- ------------------------------------------------------------------------------------------------->
        <div style="display: flex">
        
          <b-row class="justify-content-center">
            <div
              v-for="(libro, index) in libros"
              :key="index"
              class="col-sm-12 col-md-6 col-lg-4 mb-2"
            >
          
              <b-card
                
                :title="libro.titulo"
                :img-src="'http://localhost:8080/' + libro.imagen"
                img-alt="Image"
                img-top
                tag="article"
                img-height="500px"
                style="min-height: 750px;"
                :draggable="true"
                @dragstart="selectObject(libro)"
              >
                <div class="card-body">
                  <b-card-text class="mb-2 text-secondary">
                    <strong
                      ><h3>{{ libro.nombre }}</h3></strong
                    >
                  </b-card-text>
                  <b-card-text class="mb-2">
                    <strong>Autor:</strong> {{ libro.autor }}
                  </b-card-text>
                  <b-card-text class="mb-2">
                    <strong>Género:</strong> {{ libro.genero.nombre }}
                  </b-card-text>
                  <b-card-text class="mb-2">
                    <strong>Año de publicación:</strong>
                    {{ libro.anioPublicacion }}
                  </b-card-text>
                </div>
              </b-card>
            </div>
          </b-row>

          
        </div>

        <ModalActualizarLibro ref="modalActualizarLibro" />
      </div>
    </div>
    <div>
      <div
        style="width: 200px; height: 200px; "
        @drop="dropHandler($event, 'actualizar')"
        @dragover.prevent
        class="border border-warning"
      >Arrastra aquí para actualizar</div>
      <div
        style="width: 200px; height: 200px; "
        @drop="dropHandler($event, 'eliminar')"
        @dragover.prevent
        class="border border-danger"
      >Arrastra aquí para eliminar</div>
    </div>
  </div>
</template>

<script>
import ModalRegistroVue from "./components/ModalRegistro.vue";
import ModalActualizarLibro from "./components/ModalActualizarLibro.vue";

import axios from "axios";
import Swal from "sweetalert2";

const libroSeleccionado = {};

export default {
  components: {
    ModalRegistroVue,
    ModalActualizarLibro,
  },
  data() {
    return {
      libros: [],
      selected: null,
      options: [
        { value: "vacio", text: "Todos los libros" },
        { value: "Nombre", text: "Nombre" },
        { value: "Autor", text: "Autor" },
        { value: "Fechas", text: "Rango de fechas" },
      ],
    };
  },

  mounted() {
    // Llamada a la API al montar el componente
    this.obtenerLibros();
  },

  methods: {
    dropHandler(event, color) {
      event.preventDefault();
      const libroDropped = this.libroSeleccionado;
      if (color === "actualizar") {
        this.abrirModalActualizar(libroSeleccionado.value);
      } else if (color === "eliminar") {
        this.confirmarEliminar(libroSeleccionado.value.id);
      }
    },

    selectObject(libro) {
      libroSeleccionado.value = libro;
    },
    async obtenerLibros() {
      try {
        const response = await axios.get("http://localhost:8080/api/libros/");
        // Almacenar los libros en el estado del componente
        this.libros = response.data.data;
      } catch (error) {}
    },
    async obtenerLibrosNombre(nombre) {
      try {
        const response = await axios.get(
          "http://localhost:8080/api/libros/nombre/" + nombre
        );
        // Almacenar los libros en el estado del componente
        this.libros = response.data.data;
      } catch (error) {}
    },
    async obtenerLibrosAutor(nombre) {
      try {
        const response = await axios.get(
          "http://localhost:8080/api/libros/autor/" + nombre
        );
        // Almacenar los libros en el estado del componente
        this.libros = response.data.data;
      } catch (error) {}
    },
    async obtenerLibrosAnios(anioI, anioF) {
      if (anioI > anioF) {
        Swal.fire({
          title: "Upppps",
          text: "El rango de fechas es incorrecto",
          icon: "warning",
        });
      } else {
        try {
          const response = await axios.get(
            "http://localhost:8080/api/libros/anio/" + anioI + "/" + anioF
          );
          // Almacenar los libros en el estado del componente
          this.libros = response.data.data;
        } catch (error) {}
      }
    },
    abrirModalActualizar(libro) {
      this.$refs.modalActualizarLibro.abrirModal(libro);
    },
    async eliminarLibro(libroId) {
      try {
        const response = await axios.delete(
          "http://localhost:8080/api/libros/",
          {
            data: { id: libroId },
          }
        );

        const responseData = response.data;
        if (responseData.statusCode < 400) {
          Swal.fire({
            title: "Acción exitosa",
            text: "El registro se ha eliminado",
            icon: "success",
          });
          this.obtenerLibros();
        } else {
          Swal.fire({
            title: "Upps!",
            text: responseData.message,
            icon: "error",
          });
        }
      } catch (error) {
        Swal.fire({
          title: "Algo salió mal",
          text: "Favor de intentarlo mas tarde",
          icon: "error",
        });
      }
    },

    confirmarEliminar(libroId) {
      Swal.fire({
        title: "¿Esta seguro?",
        text: "¡Esta accion no podra revertirse!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#ffc107",
        cancelButtonColor: "#d33",
        confirmButtonText: "Confirmar",
        cancelButtonText: "Cancelar",
      }).then((result) => {
        if (result.isConfirmed) {
          this.eliminarLibro(libroId);
        }
      });
    },
  },
};

window.addEventListener("scroll", function () {
  const divToHide = document.querySelector(".scroll-hide");
  if (window.scrollY > 20) {
    divToHide.style.visibility = "hidden";
  } else {
    divToHide.style.visibility = "visible";
  }
});
</script>

<style scoped>
.scroll-hide {
  visibility: visible;
}

.card {
  transition: transform 0.5s ease-in-out;
}

.card:hover {
  transform: scale(1.05);
}
</style>