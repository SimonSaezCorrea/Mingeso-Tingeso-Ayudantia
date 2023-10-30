import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import HeaderComponent from "./Headers/HeaderComponent";
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import Swal from 'sweetalert2';
import EstudianteService from "../services/EstudianteService";

function AgregarEstudianteComponent(props){

    const initialState = {
        rut: "",
        nombres: "",
        apellidos: "",
        fecha_nacimiento: "",
        tipo_colegio: "",
        nombre_colegio: "",
        anio_egreso: "",
    };

    const [input, setInput] = useState(initialState);
    const navigate = useNavigate();
    const navigateHome = () => {
        navigate("/");
    };
    
    const changeRutHandler = event => {
        setInput({ ...input, rut: event.target.value });
    };
    const changeNombresHandler = event => {
        setInput({ ...input, nombres: event.target.value });
    };
    const changeApellidoHandler = event => {
        setInput({ ...input, apellidos: event.target.value });
    };
    const changeFechaNacimientoHandler = event => {
        setInput({ ...input, fecha_nacimiento: event.target.value });
    };
    const changeAnioEgresoIDHandler = event => {
        setInput({ ...input, anio_egreso: event.target.value });
    };
    const changeTipoColegioHandler = event => {
        setInput({ ...input, tipo_colegio: event.target.value });
    };
    const changeNombreColegioHandler = event => {
        setInput({ ...input, nombre_colegio: event.target.value });
    };

    
    const ingresarEstudiante = (event) => {
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
                console.log(input.title);
                let newEstudiante = {
                    rut: input.rut,
                    nombres: input.nombres,
                    apellidos: input.apellidos,
                    fecha_nacimiento: input.fecha_nacimiento,
                    tipo_colegio: input.tipo_colegio,
                    nombre_colegio: input.nombre_colegio,
                    anio_egreso: input.anio_egreso,
                    anio_ingreso: "",
                };
                console.log(newEstudiante);
                EstudianteService.createEstudiante(newEstudiante);
                Swal.fire({
                    title: "Enviado",
                    timer: 2000,
                    icon: "success",
                    timerProgressBar: true,
                    didOpen: () => {
                        Swal.showLoading()
                      },
                    })
                navigateHome();
            }
        });
    };

    return(
        <div className="general">
            <HeaderComponent />
            <div className="container-create">
                <Form>
                    <Form.Group className="mb-3" controlId="rut" value = {input.rut} onChange={changeRutHandler}>
                        <Form.Label className="agregar">Rut:</Form.Label>
                        <Form.Control className="agregar" type="text" name="rut"/>
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="nombres" value = {input.nombres} onChange={changeNombresHandler}>
                        <Form.Label className="agregar">Nombres:</Form.Label>
                        <Form.Control className="agregar" type="text" name="nombres"/>
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="apellidos" value = {input.apellido} onChange={changeApellidoHandler}>
                        <Form.Label className="agregar">Apellidos:</Form.Label>
                        <Form.Control className="agregar" type="text" name="apellidos"/>
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="fecha_nacimiento" value = {input.fecha_nacimiento} onChange={changeFechaNacimientoHandler}>
                        <Form.Label className="agregar">Fecha de Nacimiento:</Form.Label>
                        <Form.Control className="agregar" type="date" name="fecha_nacimiento"/>
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="anio_egreso" value = {input.anio_egreso} onChange={changeAnioEgresoIDHandler}>
                        <Form.Label className="agregar">Año de egreso del colegio:</Form.Label>
                        <Form.Control className="agregar" type="date" name="anio_egreso"/>
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="tipo_colegio">
                        <Form.Label className="agregar"> Tipo: </Form.Label>
                        <select className="agregar" name="tipo_colegio" required value = {input.tipo_colegio} onChange={changeTipoColegioHandler}>
                            <option value="0" disabled>Tipo colegio</option>
                            <option value="1">Municipal</option>
                            <option value="2">Subvencionado</option>
                            <option value="3">Privado</option>
                        </select>
                    </Form.Group>

                    <Form.Group className="mb-3" controlId="nombre_colegio" value = {input.nombre_colegio} onChange={changeNombreColegioHandler}>
                        <Form.Label className="agregar">Nombre del colegio:</Form.Label>
                        <Form.Control className="agregar" type="text" name="nombre_colegio"/>
                    </Form.Group>
                    <Button className="boton" onClick={ingresarEstudiante}>Registrar Proveedor</Button>
                </Form>
            </div>
        </div>
    )
}
    export default AgregarEstudianteComponent;