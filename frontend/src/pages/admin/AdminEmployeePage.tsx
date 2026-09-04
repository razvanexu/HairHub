import EmployeeForm from "../../components/forms/EmployeeForm.tsx";
import styles from "./AdminEmployeePage.module.css"
import {Link} from "react-router-dom";

function AdminEmployeePage() {
    return (
        <>
            <Link to={"/admin"} className={styles.backlink}>← Înapoi</Link>
            <h1 className={styles.title}>Adauga angajat</h1>
            <EmployeeForm/>
        </>
    )
}

export default AdminEmployeePage;