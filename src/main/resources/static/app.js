// ============================================
// 1. LÓGICA DE LOGIN (Para index.html)
// ============================================
const loginForm = document.getElementById('loginForm');
if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const user = document.getElementById('username').value;
        const pass = document.getElementById('password').value;
        const credentials = btoa(`${user}:${pass}`);

        try {
            const response = await fetch('/api/productos', {
                method: 'GET',
                headers: {
                    'Authorization': `Basic ${credentials}`
                }
            });

            if (response.ok) {
                localStorage.setItem('userAuth', credentials);
                window.location.href = 'dashboard.html';
            } else {
                document.getElementById('mensajeError').classList.remove('d-none');
            }
        } catch (error) {
            console.error('Error en la conexión:', error);
        }
    });
}

// ============================================
// 2. CARGAR PRODUCTOS EN EL DASHBOARD
// ============================================
async function cargarProductos() {
    const credentials = localStorage.getItem('userAuth');

    if (!credentials) {
        window.location.href = 'index.html';
        return;
    }

    try {
        const response = await fetch('/api/productos', {
            headers: { 'Authorization': `Basic ${credentials}` }
        });

        if (response.ok) {
            const productos = await response.json();
            const tabla = document.getElementById('tablaProductos');

            tabla.innerHTML = productos.map(p => `
                <tr>
                    <td>${p.id}</td>
                    <td>${p.codigo}</td>
                    <td>${p.nombre}</td>
                    <td class="fw-bold ${p.stock.cantidad <= 5 ? 'text-danger' : 'text-success'}">
                        ${p.stock.cantidad}
                    </td>
                    <td>$${p.precioVenta}</td>
                    <td>
                        <button class="btn btn-success btn-sm"
                                onclick="venderProducto(${p.id}, '${p.nombre}')">
                            Vender
                        </button>
                    </td>
                </tr>
            `).join('');
        }
    } catch (error) {
        console.error('Error al cargar productos:', error);
    }
}

// ============================================
// 3. FUNCIÓN DE VENTA - FORMATO CORRECTO
// ============================================
async function venderProducto(id, nombre) {
    const cantidad = prompt(`¿Cuántas unidades de "${nombre}" desea vender?`, "1");

    if (!cantidad || cantidad <= 0) {
        alert("❌ Cantidad inválida");
        return;
    }

    const credentials = localStorage.getItem('userAuth');

    // ✅ ESTRUCTURA CORRECTA: Array de detalles
    const ventaData = {
        detalles: [
            {
                producto: { id: id },
                cantidad: parseInt(cantidad)
            }
        ]
    };

    console.log("📦 Enviando venta:", JSON.stringify(ventaData, null, 2));

    try {
        const response = await fetch('/api/ventas/registrar', {
            method: 'POST',
            headers: {
                'Authorization': `Basic ${credentials}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(ventaData)
        });

        if (response.ok) {
            alert("✅ Venta registrada exitosamente");
            cargarProductos(); // Refresca la tabla
        } else {
            const errorData = await response.json();
            alert("❌ Error: " + (errorData.message || "Error al procesar la venta"));
        }
    } catch (err) {
        console.error("Error de conexión:", err);
        alert("🚀 Error de conexión con el servidor");
    }
}

// ============================================
// 4. FUNCIÓN DE CIERRE DE SESIÓN
// ============================================
function logout() {
    localStorage.removeItem('userAuth');
    window.location.href = 'index.html';
}

// ============================================
// 5. CARGAR PRODUCTOS AL INICIAR EL DASHBOARD
// ============================================
if (document.getElementById('tablaProductos')) {
    cargarProductos();
}