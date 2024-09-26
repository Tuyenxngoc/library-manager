import { useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Input, message } from 'antd';

import { IoIosMail } from 'react-icons/io';

import { useFormik } from 'formik';
import * as yup from 'yup';

import classNames from 'classnames/bind';
import styles from '~/styles/AdminLogin.module.scss';
import useAuth from '~/hooks/useAuth';
import { handleError } from '~/utils/errorHandler';
import { forgotPassword } from '~/services/authService';

const cx = classNames.bind(styles);

const validationSchema = yup.object({
    email: yup.string().trim().email('Email không hợp lệ').required('Vui lòng nhập email'),
});

const defaultValue = {
    email: '',
};

function AdminForgotPassword() {
    const navigate = useNavigate();
    const location = useLocation();

    const [messageApi, contextHolder] = message.useMessage();
    const { isAuthenticated, login } = useAuth();

    const from = location.state?.from?.pathname || '/';

    const handleLogin = async (values, { setSubmitting }) => {
        try {
            const response = await forgotPassword(values);
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
                            <h4>Quên mật khẩu</h4>
                            <p>
                                Bạn quên mật khẩu đăng nhập? Xin hãy nhập địa chỉ email đăng ký thành viên ở đây. Chúng
                                tôi sẽ gứi lại mật khẩu mới cho bạn qua Email.
                            </p>

                            <form className="mb-2" onSubmit={formik.handleSubmit}>
                                <div className="mb-3">
                                    <label htmlFor="email">Địa chỉ Email:</label>
                                    <Input
                                        addonBefore={<IoIosMail />}
                                        size="large"
                                        id="email"
                                        name="email"
                                        value={formik.values.email}
                                        onChange={formik.handleChange}
                                        onBlur={formik.handleBlur}
                                        status={formik.touched.email && formik.errors.email ? 'error' : undefined}
                                    />
                                    <div className="text-danger">{formik.touched.email && formik.errors.email}</div>
                                </div>

                                <Button
                                    size="large"
                                    type="primary"
                                    htmlType="submit"
                                    block
                                    loading={formik.isSubmitting}
                                >
                                    Gửi yêu cầu
                                </Button>
                            </form>
                            <Link to="/admin/login">Quay lại</Link>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default AdminForgotPassword;
