import axios from 'axios';

const ESTUDIANTE_API_URL = "http://localhost:8080/estudiante/";

class EstudianteService {

    getEstudiantes(){
        return axios.get(ESTUDIANTE_API_URL);
    }

    getEstudianteByRut(rut){
        return axios.get(ESTUDIANTE_API_URL + rut);
    }
    createEstudiante(estudiante){
        return axios.post(ESTUDIANTE_API_URL, estudiante);
    }
}

export default new EstudianteService()