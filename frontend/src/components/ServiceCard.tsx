import styles from './ServiceCard.module.css'

type ServiceCardProps = {
    name: string
    description: string
    price: string
}

function ServiceCard({name, description, price}: Readonly<ServiceCardProps>) {
    return (
        <div className={styles.card}>
            <div className={styles.imageContainer}></div>
            <h3>{name}</h3>
            <p>{description}</p>
            <span>{price}</span>
        </div>
    )
}

export default ServiceCard;