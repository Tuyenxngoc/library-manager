import { useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';

import { Button, Input, message } from 'antd';
import { useFormik } from 'formik';
import * as yup from 'yup';

import useAuth from '../hooks/useAuth';
import { handleError } from '../utils/errorHandler.jsx';
import { loginUser } from '../service/authService.jsx';

const validationSchema = yup.object({
    username: yup.string().trim().required('Vui lòng nhập tên tài khoản'),

    password: yup.string().required('Vui lòng nhập mật khẩu'),
});

const defaultValue = {
    username: '',
    password: '',
};

function Login() {
    const navigate = useNavigate();
    const location = useLocation();

    const [messageApi, contextHolder] = message.useMessage();
    const { isAuthenticated, login } = useAuth();

    const from = location.state?.from?.pathname || '/';
    const handleLogin = async (values, { setSubmitting }) => {
        try {
            const response = await loginUser(values);
            if (response.status === 200) {
                const { accessToken, refreshToken } = response.data.data;
                login({ accessToken, refreshToken });
                navigate(from, { replace: true });
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
        onSubmit: handleLogin,
    });

    useEffect(() => {
        if (isAuthenticated) {
            navigate('/', { replace: true });
        }
    }, [isAuthenticated, navigate]);

    return (
        <>
            {contextHolder}

            <form onSubmit={formik.handleSubmit}>
                <div className="mb-2">
                    <label htmlFor="username">Tên tài khoản</label>
                    <Input
                        id="username"
                        autoComplete="on"
                        value={formik.values.username}
                        onChange={formik.handleChange}
                        onBlur={formik.handleBlur}
                        status={formik.touched.username && formik.errors.username ? 'error' : undefined}
                    />
                    {formik.touched.username && formik.errors.username && (
                        <div className="text-danger">{formik.errors.username}</div>
                    )}
                </div>

                <div className="mb-2">
                    <label htmlFor="password">Mật khẩu</label>
                    <Input.Password
                        id="password"
                        value={formik.values.password}
                        onChange={formik.handleChange}
                        onBlur={formik.handleBlur}
                        status={formik.touched.password && formik.errors.password ? 'error' : undefined}
                    />
                    {formik.touched.password && formik.errors.password && (
                        <div className="text-danger">{formik.errors.password}</div>
                    )}
                </div>

                <Button type="primary" htmlType="submit" loading={formik.isSubmitting}>
                    Đăng nhập
                </Button>
                <Button>Đăng nhập với Google</Button>
            </form>

            <div>
                <div>
                    <span> Nếu bạn chưa có tài khoản, vui lòng đăng ký </span>
                    <Link to="/register">Đăng ký</Link>
                </div>
                <Link to="/forget-password">Quên mật khẩu</Link>
            </div>
        </>
    );
}

export default Login;
