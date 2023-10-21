import axios from 'axios';

const CUOTAS_API_URL = "http://localhost:8080/cuotas/";

class CuotasService {

    generarCuotas(rut, cuotas){
        return axios.get(CUOTAS_API_URL + rut +"/"+cuotas);
    }
    getCuotas(rut){
        return axios.get(CUOTAS_API_URL + rut);
    }

}
    
export default new CuotasService()