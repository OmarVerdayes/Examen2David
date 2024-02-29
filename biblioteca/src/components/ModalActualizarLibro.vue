<template>
  <b-modal v-model="showModal" title="Actualizar Libro">
    <b-form @submit.prevent="actualizarLibro">
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
        </div>
      </b-form-group>

      <b-form-group label="Año de publicación:" label-for="anioPublicacion">
        <b-form-input
          v-model="formulario.anioPublicacion"
          id="anioPublicacion"
          type="number"
          field="nombre"
          required
        />
      </b-form-group>

      <b-form-group
        label="Imagen(si no desea actualizar la imagen déjela vacía):"
        label-for="imagen"
      >
        <b-form-file
          v-model="formulario.imagenArchivo"
          id="imagenArchivo"
          accept="image/*"
        />
      </b-form-group>

      <b-button type="submit" variant="primary">Actualizar</b-button>
      <b-button variant="danger" @click="cerrarModal">Cancelar</b-button>
    </b-form>
  </b-modal>
</template>

<script>
import axios from "axios";
import Swal from "sweetalert2";

export default {
  data() {
    return {
      showModal: false,
      formulario: {
        id: null,
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
    abrirModal(libro) {
      // Clonar el objeto para evitar modificaciones directas
      this.formulario = { ...libro };

      // Asegurarse de que el valor de 'genero' en el formulario coincida con el del libro
      this.formulario.genero = libro.genero.id;

      this.showModal = true;
    },
    cerrarModal() {
      this.showModal = false;
    },
    async actualizarLibro() {
      try {
        const formData = new FormData();
        formData.append("id", this.formulario.id);
        formData.append("nombre", this.formulario.nombre);
        formData.append("autor", this.formulario.autor);
        formData.append("genero", this.formulario.genero.id);
        formData.append("anioPublicacion", this.formulario.anioPublicacion);

        console.log(this.formulario.imagenArchivo);
        if (this.formulario.imagenArchivo) {
          formData.append("imagenArchivo", this.formulario.imagenArchivo);
        }

        const response = await axios.put(
          `http://localhost:8080/api/libros/`,
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
            text: "El libro se actualizó correctamente",
            icon: "success",
          });
        } else {
          Swal.fire({
            title: "Upps!",
            text: responseData.message,
            icon: "error",
          });
        }
        this.cerrarModal();
      } catch (error) {
        Swal.fire({
          title: "Error",
          text: "Hubo un error al actualizar el libro. Por favor, inténtalo de nuevo más tarde.",
          icon: "error",
        });
      }
    },
  },
};
</script>
