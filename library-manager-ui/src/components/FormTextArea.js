import { Input } from 'antd';
const { TextArea } = Input;

const FormTextArea = ({ id, label, className, formik, required }) => (
    <div className={className}>
        <label htmlFor={id}>
            {required && <span className="text-danger">*</span>} {label}:
        </label>
        <TextArea
            rows={4}
            id={id}
            name={id}
            value={formik.values[id]}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            status={formik.touched[id] && formik.errors[id] ? 'error' : undefined}
        />
        <div className="text-danger">{formik.touched[id] && formik.errors[id]}</div>
    </div>
);

export default FormTextArea;
