import { useNavigate, useParams } from 'react-router-dom';
import { useFormik } from 'formik';
import * as yup from 'yup';
import { Button, Input, message } from 'antd';
import { handleError } from '~/utils/errorHandler';
import { getAuthorById, updateAuthor } from '~/services/authorService';
import { useEffect } from 'react';
import { checkIdIsNumber } from '~/utils/helper';

const defaultValue = {
    id: 0,
    fullName: '',
    code: '',
    penName: '',
    gender: 'MALE',
    dateOfBirth: '2024-09-26',
    dateOfDeath: '2024-09-26',
    title: '',
    residence: '',
    address: '',
    notes: '',
};

const validationSchema = yup.object({});

function AuthorForm() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [messageApi, contextHolder] = message.useMessage();

    const handleSubmit = async (values, { setSubmitting }) => {
        try {
            const response = await updateAuthor(id, values);
            if (response.status === 200) {
                messageApi.success(response.data.data.message);
            }
        } catch (error) {
            handleError(error, formik, messageApi);
        } finally {
            setSubmitting(false);
        }
    };

    const formik = useFormik({
        initialValues: defaultValue,
        validationSchema: validationSchema,
        onSubmit: handleSubmit,
        enableReinitialize: true,
    });

    useEffect(() => {
        if (id) {
            // Nếu có id, lấy thông tin tác giả để sửa
            getAuthorById(id)
                .then((response) => {
                    const {
                        id,
                        fullName,
                        code,
                        penName,
                        gender,
                        dateOfBirth,
                        dateOfDeath,
                        title,
                        residence,
                        address,
                        notes,
                    } = response.data.data;

                    formik.setValues({
                        id,
                        fullName,
                        code,
                        penName,
                        gender,
                        dateOfBirth,
                        dateOfDeath,
                        title,
                        residence,
                        address,
                        notes,
                    });
                })
                .catch((error) => {
                    console.log(error);
                });
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [id]);

    return (
        <>
            {contextHolder}
            <h2>{id ? 'Chỉnh sửa tác giả' : 'Thêm mới tác giả'}</h2>

            <div className="mb-3">
                <label htmlFor="fullName">Họ tên:</label>
                <Input
                    size="large"
                    id="fullName"
                    name="fullName"
                    value={formik.values.fullName}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    status={formik.touched.fullName && formik.errors.fullName ? 'error' : undefined}
                />
                <div className="text-danger">{formik.touched.fullName && formik.errors.fullName}</div>
            </div>
        </>
    );
}

export default AuthorForm;
