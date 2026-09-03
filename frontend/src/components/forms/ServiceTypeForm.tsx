import {useState} from "react";
import styles from './ServiceTypeForm.module.css'

function ServiceTypeForm() {
    const [name, setName] = useState("");
    const [price, setPrice] = useState("");
    const [duration, setDuration] = useState("");
    const [description, setDescription] = useState("");
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState(false);

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault();
        setError(null);
        setSuccess(false);

        try {
            const response = await fetch("http://localhost:8080/service-type", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({name, price: Number(price), duration: Number(duration), description}),
            })

            if (!response.ok) {
                throw new Error(response.statusText);
            }
            setSuccess(true);
            setName("")
            setPrice("")
            setDuration("")
            setDescription("")
        } catch (err) {
            setError("Nu s-a salvat");
        }
    }

    return (
        <form onSubmit={handleSubmit} className={styles.form}>
            {error && <p className={styles.error}>{error}</p>}
            {success && <p className={styles.success}>Serviciu salvat</p>}
            <input className={styles.input}
                   type="text"
                   placeholder="Service Name"
                   name="name"
                   value={name}
                   onChange={(e) => setName(e.target.value)}
            />

            <input className={styles.input}
                   type="number"
                   placeholder="Service Price"
                   name="price"
                   value={price}
                   onChange={(e) => setPrice(e.target.value)}
            />

            <input className={styles.input}
                   type="number"
                   placeholder="Service Duration"
                   name="duration"
                   value={duration}
                   onChange={(e) => setDuration(e.target.value)}
            />

            <textarea className={styles.textArea}
                      placeholder="Service Description"
                      name="description"
                      value={description}
                      onChange={(e) =>
                          setDescription(e.target.value)}
                      rows={4}
            />
            <button type={"submit"} className={styles.submitButton}>Submit</button>
        </form>
    )
}

export default ServiceTypeForm;