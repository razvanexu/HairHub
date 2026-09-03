import styles from './Header.module.css'
import {Link} from "react-router-dom";

function Header() {
    return (
        <div className={styles.header}>
            <span className={styles.logo}>HairHub</span>
            <nav className={styles.nav}>
                <a href="/">Servicii</a>
                <a href="/">Echipa</a>
                <a href="/">Galerie</a>
                <a href="/">Contact</a>
                <Link to={"/admin"}>Admin</Link>
            </nav>
            <button type={"button"} className={styles.bookButton}>
                Programeaza-te
            </button>
        </div>
    )
}

export default Header;