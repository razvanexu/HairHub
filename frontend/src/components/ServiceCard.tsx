import styles from './ServiceCard.module.css'

type ServiceCardProps = {
    name: string
    description: string
    duration: string
    price: string
}

function ServiceCard({name, description, duration, price}: Readonly<ServiceCardProps>) {
    return (
        <div className={styles.card}>
            <div className={styles.imageContainer}></div>
            <h3>{name}</h3>
            <p>{description}</p>
            <span>{duration}</span>
            <span>{price}</span>
        </div>
    )
}

export default ServiceCard;