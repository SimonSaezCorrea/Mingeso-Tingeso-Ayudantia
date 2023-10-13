import axios from 'axios';

const GRASAYSOLIDOSTOTALES_API_URL = "http://localhost:8080/cuotas/";

class GrasaYSolidosTotalesService {

    getCuotas(rut){
        return axios.get(GRASAYSOLIDOSTOTALES_API_URL + rut);
    }

    createGrasaYSolidosTotales(acopioLeche){
        return axios.post(GRASAYSOLIDOSTOTALES_API_URL, acopioLeche);
    }

    CargarArchivoGrasaYSolido(file){
        return axios.post(GRASAYSOLIDOSTOTALES_API_URL + "/subirGrasaSolidoTotal", file);
    }
}

export default new GrasaYSolidosTotalesService()