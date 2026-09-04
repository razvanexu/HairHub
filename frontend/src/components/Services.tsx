import ServiceCard from "./ServiceCard.tsx";
import styles from "./Services.module.css"
import {useEffect, useState} from "react";

type ServiceType = {
    id: number;
    name: string;
    price: number;
    duration: number;
    description: string;
}

function Services() {
    const [services, setServices] = useState<ServiceType[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(false);

    useEffect(() => {
        fetch("http://localhost:8080/service-type")
            .then(res => res.json())
            .then(data => setServices(data))
            .catch(() => setError(true))
            .finally(() => setLoading(false));
    }, [])

    return (
        <section className={styles.service}>
            <h3 className={styles.title}>Servicii, nu doar programari</h3>
            {loading && <p>Loading...</p>}
            {error && <p>Nu am putut incarca serviciile</p>}
            {!loading && !error && (
                <div className={styles.cards}>
                    {services.map((service) => (
                        <ServiceCard
                            key={service.id}
                            name={service.name}
                            description={service.description}
                            duration={`aproximativ ${service.duration} de minute`}
                            price={`de la ${Number(service.price)} lei`}/>
                    ))}
                </div>
            )}
        </section>
    )
}

export default Services;