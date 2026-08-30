import ServiceCard from "./ServiceCard.tsx";
import styles from "./Services.module.css"

function Services() {
    return (
        <section className={styles.service}>
            <h3 className={styles.title}>Servicii, nu doar programari</h3>
            <div className={styles.cards}>
                <ServiceCard name="Refresh" description="Tuns + Styling" price="de la 50 lei"/>
                <ServiceCard name="Gloss & Glow" description="Tuns, tratament si Styling" price="de la 100 lei"/>
                <ServiceCard name="Transform" description="Vopsit + Tuns + Styling" price="de la 250 lei"/>
            </div>
        </section>
    )
}

export default Services;