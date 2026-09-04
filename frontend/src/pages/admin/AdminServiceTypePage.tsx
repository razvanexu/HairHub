import ServiceTypeForm from "../../components/forms/ServiceTypeForm.tsx";
import styles from "./AdminServiceTypePage.module.css"
import {Link} from "react-router-dom";

function AdminServiceTypePage() {
    return (
        <>
            <Link to={"/admin"} className={styles.backlink}>← Înapoi</Link>
            <h1 className={styles.title}>Adauga un serviciu</h1>
            <ServiceTypeForm/>

        </>
    )
}

export default AdminServiceTypePage;