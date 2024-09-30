import { Input } from 'antd';

const FormInput = ({ id, label, className, formik, required = false }) => (
    <div className={className}>
        <label htmlFor={id}>
            {required && <span className="text-danger">*</span>} {label}:
        </label>
        <Input
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

export default FormInput;
