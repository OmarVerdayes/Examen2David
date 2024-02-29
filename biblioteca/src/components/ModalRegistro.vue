<template>
  <div style="display: flex">
    <div :draggable="true">
      <form @submit.prevent="registrarLibro">
        <b-form-group label="Nombre:" label-for="nombre">
          <b-form-input v-model="formulario.nombre" id="nombre" required />
        </b-form-group>

        <b-form-group label="Autor:" label-for="autor">
          <b-form-input v-model="formulario.autor" id="autor" required />
        </b-form-group>

        <b-form-group label="Género:" label-for="genero">
          <div class="d-flex">
            <b-form-select
              v-model="formulario.genero"
              id="genero"
              :options="opcionesGenero"
              field="id"
              required
            />
            <ModalregistroGenero />
          </div>
        </b-form-group>

        <b-form-group label="Año de publicación:" label-for="anioPublicacion">
          <b-form-input
            v-model="formulario.anioPublicacion"
            id="anioPublicacion"
            type="number"
            max="2024"
            field="nombre"
            required
          />
        </b-form-group>

        <b-form-group label="Imagen:" label-for="imagen">
          
          <b-form-file
            v-model="formulario.imagenArchivo"
            id="imagenArchivo"
            accept="image/*"
            required
          />
        </b-form-group>
      </form>
    </div>
    <div>

      <div
        style="width: 200px; height: 200px; background-color: rgba(255, 255, 255, 0)"
        @drop="dropHandler()"
        @dragover.prevent
        class="border border-primary"
      >Arrastra para Registrar</div>

      
    </div>
  </div>
</template>

<script>
import axios from "axios";
import Swal from "sweetalert2";
import ModalregistroGenero from "./ModalregistroGenero.vue";

export default {
  components: {
    ModalregistroGenero,
  },
  data() {
    return {
      formulario: {
        nombre: "",
        autor: "",
        genero: "",
        anioPublicacion: null,
        imagenArchivo: null,
      },
      opcionesGenero: [],
    };
  },

  mounted() {
    this.obtenerGeneros();
  },
  methods: {
    async obtenerGeneros() {
      try {
        const response = await axios.get("http://localhost:8080/api/generos/");
        const generos = response.data.data;

        this.opcionesGenero = generos.map((genero) => {
          return {
            value: {
              id: genero.id,
            },
            text: genero.nombre,
          };
        });
      } catch (error) {}
    },
    async registrarLibro() {
      await registrar(this.formulario);
    },
    dropHandler() {
      this.registrarLibro();
    },
  },
};

const registrar = async (informacion) => {
  if (
    (informacion.nombre && informacion.autor && informacion.genero.id,
    informacion.anioPublicacion && informacion.imagenArchivo)
  ) {
    try {
      const formData = new FormData();

      // Agregar campos de texto al FormData
      formData.append("nombre", informacion.nombre);
      formData.append("autor", informacion.autor);
      formData.append("genero", informacion.genero.id);
      formData.append("anioPublicacion", informacion.anioPublicacion);

      // Agregar archivo al FormData
      if (informacion.imagenArchivo) {
        formData.append("imagenArchivo", informacion.imagenArchivo);
      }

      const response = await axios.post(
        "http://localhost:8080/api/libros/",
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );

      const responseData = response.data;
      if (responseData.statusCode < 400) {
        Swal.fire({
          title: "Acción exitosa",
          text: "Registro exitoso",
          icon: "success",
        });
      } else {
        Swal.fire({
          title: "Upps!",
          text: responseData.message,
          icon: "error",
        });
      }
    } catch (error) {
      console.log(error);
      Swal.fire({
        title: "Algo salió mal",
        text: "Favor de intentarlo más tarde",
        icon: "error",
      });
    }
  } else {
    Swal.fire({
      title: "uppps!",
      text: "Favor llenar todos los datos",
      icon: "warning",
    });
  }
};
</script>

<style scoped>
</style>
