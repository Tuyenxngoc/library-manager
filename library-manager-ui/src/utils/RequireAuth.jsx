import PropTypes from 'prop-types';
import { Navigate, Outlet, useLocation } from 'react-router-dom';
import useAuth from '../hooks/useAuth';

function RequireAuth({ allowedRoles }) {
    const { isAuthenticated, user } = useAuth();
    const location = useLocation();

    if (isAuthenticated) {
        if (allowedRoles && allowedRoles.length > 0) {
            const hasRequiredRole = allowedRoles.some((role) => user.roleName === role);

            if (!hasRequiredRole) {
                return <Navigate to="/access-denied" />;
            }
        }
        return <Outlet />;
    } else {
        return <Navigate to="/login" state={{ from: location }} replace />;
    }
}

RequireAuth.propTypes = {
    allowedRoles: PropTypes.arrayOf(PropTypes.string),
};

export default RequireAuth;
