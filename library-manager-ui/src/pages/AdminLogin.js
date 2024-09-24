import { useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Checkbox, Input, message } from 'antd';

import { FaUser, FaKey } from 'react-icons/fa';

import { useFormik } from 'formik';
import * as yup from 'yup';

import classNames from 'classnames/bind';
import styles from '~/styles/AdminLogin.module.scss';
import useAuth from '~/hooks/useAuth';
import { loginUser } from '~/services/authService';
import { handleError } from '~/utils/errorHandler';

const cx = classNames.bind(styles);

const validationSchema = yup.object({
    username: yup.string().trim().required('Vui lòng nhập tên tài khoản'),

    password: yup.string().required('Vui lòng nhập mật khẩu'),
});

const defaultValue = {
    username: '',
    password: '',
};

function AdminLogin() {
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
        <div className="container">
            {contextHolder}

            <div className="row justify-content-center">
                <div className="col-4">
                    <div className={cx('login-panel')}>
                        <div className={cx('panel-heading')}>
                            <h3 className="panel-title text-center mb-0">
                                <img
                                    src="https://media.licdn.com/dms/image/v2/C560BAQE6t6jYd0fJpA/company-logo_100_100/company-logo_100_100/0/1631316008209?e=2147483647&v=beta&t=9O8BlvyaqqdaT-z51Kn1etrOerKtptsERA90Q1aSENU"
                                    alt=""
                                    style={{ height: 40 }}
                                />
                            </h3>
                        </div>

                        <div className={cx('panel-body')}>
                            <form className="mb-2" onSubmit={formik.handleSubmit}>
                                <div className="mb-3">
                                    <label htmlFor="username">Tên đăng nhập:</label>
                                    <Input
                                        addonBefore={<FaUser />}
                                        size="large"
                                        id="username"
                                        name="username"
                                        value={formik.values.username}
                                        onChange={formik.handleChange}
                                        onBlur={formik.handleBlur}
                                        status={formik.touched.username && formik.errors.username ? 'error' : undefined}
                                    />
                                    <div className="text-danger">
                                        {formik.touched.username && formik.errors.username}
                                    </div>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="password">Mật khẩu:</label>
                                    <Input.Password
                                        addonBefore={<FaKey />}
                                        size="large"
                                        id="password"
                                        name="password"
                                        value={formik.values.password}
                                        onChange={formik.handleChange}
                                        onBlur={formik.handleBlur}
                                        status={formik.touched.password && formik.errors.password ? 'error' : undefined}
                                    />
                                    <div className="text-danger">
                                        {formik.touched.password && formik.errors.password}
                                    </div>
                                </div>
                                <div className="mb-3">
                                    <Checkbox checked>Nhớ mật khẩu</Checkbox>
                                </div>

                                <Button
                                    size="large"
                                    type="primary"
                                    htmlType="submit"
                                    block
                                    loading={formik.isSubmitting}
                                >
                                    Đăng nhập
                                </Button>
                            </form>
                            <Link to="/admin/forgot-password">Quên mật khẩu?</Link>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default AdminLogin;
