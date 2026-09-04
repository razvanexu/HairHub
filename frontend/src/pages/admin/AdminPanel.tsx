import {Link} from "react-router-dom";
import styles from "./AdminPanel.module.css";

function AdminPanel() {
    return (
        <div className={styles.container}>
            <h1 className={styles.title}>Panou de Administrare</h1>
            <Link to="/admin/employees" className={styles.link}>Employees</Link>
            <Link to="/admin/service-types" className={styles.link}>Service Types</Link>
            <Link to="/" className={styles.backlink}>Main Page</Link>
        </div>
    )
}

export default AdminPanel;