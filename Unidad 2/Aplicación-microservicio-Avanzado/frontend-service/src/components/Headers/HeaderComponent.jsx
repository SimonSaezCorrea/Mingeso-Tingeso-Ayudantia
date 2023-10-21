import React from 'react'
import { useNavigate } from 'react-router-dom';

function HeaderComponent() {
    const navigate = useNavigate();
    const handleClick = () => {
        navigate("/");
    }
    return (
        <div>
            <header className="header">
                <div className="logo">
                    <h1 onClick={handleClick}>MilkStgo</h1>
                </div>
                <nav></nav>
                <a className="btn" href="/agregar_estudiante"><button>Ingresar nuevo Proveedor</button></a>
            </header>
        </div>
    )
}

export default HeaderComponent
