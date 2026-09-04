import './App.css'
import {BrowserRouter, Route, Routes} from "react-router-dom";
import HomePage from "./pages/HomePage.tsx";
import AdminPanel from "./pages/admin/AdminPanel.tsx";
import AdminEmployeePage from "./pages/admin/AdminEmployeePage.tsx";
import AdminServiceTypePage from "./pages/admin/AdminServiceTypePage.tsx";


function App() {
    return (<BrowserRouter>
            <Routes>
                <Route path="/" element={<HomePage/>}/>
                <Route path="/admin" element={<AdminPanel/>}/>
                <Route path="/admin/employees" element={<AdminEmployeePage/>}/>
                <Route path="/admin/service-types" element={<AdminServiceTypePage/>}/>
            </Routes>
        </BrowserRouter>
    )
}

export default App