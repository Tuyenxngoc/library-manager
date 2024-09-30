import { Button, Input, Select, Space } from 'antd';
import { useNavigate, useParams } from 'react-router-dom';
import { useFormik } from 'formik';
import * as yup from 'yup';
import queryString from 'query-string';
const defaultValue = {
    title: '',
};

const validationSchema = yup.object({});

function InwardBookForm() {
    const { id } = useParams();
    const navigate = useNavigate();

    const handleSubmit = async (values, { setSubmitting }) => {};

    const formik = useFormik({
        initialValues: defaultValue,
        validationSchema: validationSchema,
        onSubmit: handleSubmit,
        enableReinitialize: true,
    });

    return (
        <div>
            <h2>Thêm phiếu nhập</h2>

            <form onSubmit={formik.handleSubmit}>
                <div className="row g-3">
                    <div className="col-md-6">
                        <label htmlFor="title">
                            <span className="text-danger">*</span> Số phiếu nhập:
                        </label>
                        <Input
                            id="title"
                            name="title"
                            value={formik.values.title}
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            status={formik.touched.title && formik.errors.title ? 'error' : undefined}
                        />
                        <div className="text-danger">{formik.touched.title && formik.errors.title}</div>
                    </div>

                    <div className="col-md-3">
                        <label htmlFor="title">
                            <span className="text-danger">*</span> Ngày nhập:
                        </label>
                        <Input
                            id="title"
                            name="title"
                            value={formik.values.title}
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            status={formik.touched.title && formik.errors.title ? 'error' : undefined}
                        />
                        <div className="text-danger">{formik.touched.title && formik.errors.title}</div>
                    </div>

                    <div className="col-md-3">
                        <label htmlFor="title">
                            <span className="text-danger">*</span> Nguồn cấp:
                        </label>
                        <Input
                            id="title"
                            name="title"
                            value={formik.values.title}
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            status={formik.touched.title && formik.errors.title ? 'error' : undefined}
                        />
                        <div className="text-danger">{formik.touched.title && formik.errors.title}</div>
                    </div>

                    <div className="col-md-6">
                        <label htmlFor="title">Số vào tổng quát:</label>
                        <Input
                            id="title"
                            name="title"
                            value={formik.values.title}
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            status={formik.touched.title && formik.errors.title ? 'error' : undefined}
                        />
                        <div className="text-danger">{formik.touched.title && formik.errors.title}</div>
                    </div>

                    <div className="col-md-6">
                        <label htmlFor="title">Lý do nhập:</label>
                        <Input
                            id="title"
                            name="title"
                            value={formik.values.title}
                            onChange={formik.handleChange}
                            onBlur={formik.handleBlur}
                            status={formik.touched.title && formik.errors.title ? 'error' : undefined}
                        />
                        <div className="text-danger">{formik.touched.title && formik.errors.title}</div>
                    </div>

                    <div className="col-md-12 text-end">
                        <Space>
                            <Button onClick={() => navigate('/admin/books/inward')}>Quay lại</Button>
                            <Button type="primary" htmlType="submit" loading={formik.isSubmitting}>
                                Lưu
                            </Button>
                        </Space>
                    </div>
                </div>
            </form>
        </div>
    );
}

export default InwardBookForm;
