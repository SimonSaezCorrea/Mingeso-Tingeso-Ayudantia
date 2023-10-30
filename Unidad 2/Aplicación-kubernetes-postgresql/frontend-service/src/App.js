import React from "react";
import "./App.css";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import MainComponent from "./components/MainComponent";
import AgregarEstudianteComponent from "./components/AgregarEstudianteComponent";
import ListadoEstudianteComponent from "./components/ListadoEstudianteComponent";
import GenerarCuotasComponent from "./components/GenerarCuotasComponent";

function App() {
    return (
        <div>
            <Router>
                <Routes>
                    <Route path="/" element={<MainComponent />} />
                    <Route path="/agregar_estudiante" element={<AgregarEstudianteComponent />} />
                    <Route path="/lista_estudiantes" element={<ListadoEstudianteComponent />} />
                    <Route path="/generar_cuotas" element={<GenerarCuotasComponent />} />
                </Routes>
            </Router>
        </div>
    );
}

export default App;
