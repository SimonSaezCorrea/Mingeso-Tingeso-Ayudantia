import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import HeaderComponent from "./Headers/HeaderComponent";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Swal from "sweetalert2";
import CuotasService from "../services/CuotasService";
import EstudianteService from "../services/EstudianteService";

function GenerarCuotasComponent() {
    const initialState = {
        rut: "",
        cuotas: "",
        comprobar: 0,
        comprobarYaEntro: 1,
    };

    const [estudianteEntity, setEstudianteEntity] = useState();
    const [cuotasteEntity, setCuotasEntity] = useState([]);
    //const [input, setInput] = useState(initialState);
    const [input, setInput] = useState(initialState);
    const navigate = useNavigate();

    const navigateList = () => {
        navigate("/lista_cuotas");
    }

    const changeRutHandler = (event) => {
        setInput({ ...input, rut: event.target.value });
    };
    const changeCuotasHandler = (event) => {
        setInput({ ...input, cuotas: event.target.value });
    };

    const ingresarCuotas = (event) => {
        if (!input.cuotas) {
            Swal.fire("Seleccione un número de cuotas", "", "error");
            return;
        }
        Swal.fire({
            title: "¿Desea registrar el estudiante?",
            text: "No podra cambiarse en caso de equivocación",
            icon: "question",
            showDenyButton: true,
            confirmButtonText: "Confirmar",
            confirmButtonColor: "rgb(68, 194, 68)",
            denyButtonText: "Cancelar",
            denyButtonColor: "rgb(190, 54, 54)",
        }).then((result) => {
            if (result.isConfirmed) {
                console.log("rut: " + input.rut + "\n");
                console.log("cuotas: " + input.cuotas + "\n");
                let cuotas = CuotasService.generarCuotas(input.rut, input.cuotas);
                input.comprobar = 1;
                console.log("cuotas"+cuotas+"\n");
                if(cuotas){
                    Swal.fire({
                        title: "Ya tiene cuotas o no existe el usuario",
                        timer: 2000,
                        icon: "error",
                        timerProgressBar: true,
                        didOpen: () => {
                            Swal.showLoading();
                        },
                    });
                }
                else{
                    Swal.fire({
                        title: "Enviado",
                        timer: 2000,
                        icon: "success",
                        timerProgressBar: true,
                        didOpen: () => {
                            Swal.showLoading();
                        },
                    });
                    navigateList();
                }
            }
        });
    };

    return (
        <div>
            <HeaderComponent></HeaderComponent>
            <div class="contenedor">
                <div class="Alinear">
                    <Form>
                        <Form.Group className="mb-3" controlId="rut" value={input.rut} onChange={changeRutHandler}>
                            <Form.Label className="agregar">Rut:</Form.Label>
                            <Form.Control className="agregar" type="text" name="rut" />
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="Cuotas">
                            <Form.Label className="Cuotas"> Cuotas: </Form.Label>
                            <select
                                id="Cuotas"
                                name="Cuotas"
                                required
                                value={input.cuotas}
                                onChange={changeCuotasHandler}
                            >
                                <option value="Cuotas" disabled selected>
                                    Cuotas
                                </option>
                                <option value="1">1</option>
                                <option value="2">2</option>
                                <option value="3">3</option>
                                <option value="4">4</option>
                                <option value="5">5</option>
                                <option value="6">6</option>
                                <option value="7">7</option>
                                <option value="8">8</option>
                                <option value="9">9</option>
                                <option value="10">10</option>
                            </select>
                        </Form.Group>
                        <Button id="Generar" className="Generar" onClick={ingresarCuotas}>
                            Generar
                        </Button>
                    </Form>
                </div>
            </div>
        </div>
    );
}

export default GenerarCuotasComponent;
