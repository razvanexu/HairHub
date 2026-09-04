import {useState} from "react";
import styles from './EmployeeForm.module.css'

function EmployeeForm() {
    const [name, setName] = useState('')
    const [phone, setPhone] = useState('')
    const [email, setEmail] = useState('')
    const [error, setError] = useState<string | null>(null)
    const [success, setSuccess] = useState(false)

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault()
        setError(null)
        setSuccess(false)

        try {
            const response = await fetch("http://localhost:8080/employee", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({name, phone, email}),
            })

            if (!response.ok) {
                throw new Error(response.statusText)
            }
            setSuccess(true)
            setName("")
            setPhone("")
            setEmail("")
        } catch (err) {
            setError("Nu s-a salvat");
        }
    }

    return (
        <form onSubmit={handleSubmit} className={styles.form}>
            {error && <p className={styles.error}>{error}</p>}
            {success && <p className={styles.success}>Angajat salvat</p>}
            <input className={styles.input}
                   type="text"
                   placeholder="Employee Name"
                   name="employeeName"
                   value={name}
                   onChange={(e) => setName(e.target.value)}
            />
            <input className={styles.input}
                   type="text"
                   placeholder="Phone Number"
                   name="phoneNumber"
                   value={phone}
                   onChange={(e) => setPhone(e.target.value)}
            />
            <input className={styles.input}
                   type="email"
                   placeholder="Email"
                   name="email"
                   value={email}
                   onChange={(e) => setEmail(e.target.value)}
            />
            <button type={"submit"} className={styles.submitButton}>Submit</button>
        </form>
    )
}

export default EmployeeForm