
<template>
  <div>
    <b-button @click="mostrarModal" variant="success">
      <b-icon-plus></b-icon-plus>
    </b-button>

    <b-modal v-model="mostrarModalGenero" title="Registrar Genero">
      <form @submit.prevent="registrarGenero">
        <b-form-group label="Nombre:" label-for="nombre">
          <b-form-input
            v-model="formulario.nombre"
            id="nombre"
            required
          ></b-form-input>
        </b-form-group>

        <b-button type="submit" variant="primary">Registrar</b-button>
      </form>
    </b-modal>
  </div>
</template>
  
  <script>
import axios from "axios";
import Swal from "sweetalert2";

export default {
  data() {
    return {
      mostrarModalGenero: false,
      formulario: {
        nombre: "",
      },
    };
  },
  methods: {
    mostrarModal() {
      this.mostrarModalGenero = true;
    },
    cerrarModal() {
      this.mostrarModalGenero = false;
    },
    registrarGenero() {
      registrarGen(this.formulario);
      this.cerrarModal();
    },
  },
};

const registrarGen = async (informacion) => {
  try {
    const info = JSON.parse(JSON.stringify(informacion, null, 2));
    const response = await axios.post("http://localhost:8080/api/generos/", info);

    const responseData = response.data;
    if (responseData.statusCode < 400) {
      Swal.fire({
        title: "Acción exitosa",
        text: "Registro exitosos",
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
    Swal.fire({
      title: "Algo salió mal",
      text: "Favor de intentarlo mas tarde",
      icon: "error",
    });
  }
};
</script>
  
  <style scoped>
</style>
  